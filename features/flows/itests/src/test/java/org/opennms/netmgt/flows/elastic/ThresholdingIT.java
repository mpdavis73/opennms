/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2021 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 2021-2021 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/

package org.opennms.netmgt.flows.elastic;

import static com.jayway.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.opennms.core.utils.InetAddressUtils.addr;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opennms.core.cache.CacheConfigBuilder;
import org.opennms.core.spring.BeanUtils;
import org.opennms.core.test.OpenNMSJUnit4ClassRunner;
import org.opennms.core.test.db.MockDatabase;
import org.opennms.core.test.db.annotations.JUnitTemporaryDatabase;
import org.opennms.core.test.elastic.ElasticSearchRule;
import org.opennms.core.utils.InetAddressUtils;
import org.opennms.features.jest.client.RestClientFactory;
import org.opennms.features.jest.client.index.IndexStrategy;
import org.opennms.features.jest.client.template.IndexSettings;
import org.opennms.netmgt.collectd.DefaultResourceTypeMapper;
import org.opennms.netmgt.collection.core.DefaultCollectionAgentFactory;
import org.opennms.netmgt.config.dao.thresholding.api.OverrideableThreshdDao;
import org.opennms.netmgt.config.dao.thresholding.api.OverrideableThresholdingDao;
import org.opennms.netmgt.config.threshd.Filter;
import org.opennms.netmgt.config.threshd.Group;
import org.opennms.netmgt.config.threshd.IncludeRange;
import org.opennms.netmgt.config.threshd.Package;
import org.opennms.netmgt.config.threshd.Parameter;
import org.opennms.netmgt.config.threshd.Service;
import org.opennms.netmgt.config.threshd.ServiceStatus;
import org.opennms.netmgt.config.threshd.ThreshdConfiguration;
import org.opennms.netmgt.config.threshd.Threshold;
import org.opennms.netmgt.config.threshd.ThresholdType;
import org.opennms.netmgt.config.threshd.ThresholdingConfig;
import org.opennms.netmgt.dao.DatabasePopulator;
import org.opennms.netmgt.dao.api.InterfaceToNodeCache;
import org.opennms.netmgt.dao.mock.MockEventIpcManager;
import org.opennms.netmgt.dao.mock.MockNodeDao;
import org.opennms.netmgt.dao.mock.MockSessionUtils;
import org.opennms.netmgt.dao.mock.MockSnmpInterfaceDao;
import org.opennms.netmgt.events.api.EventConstants;
import org.opennms.netmgt.flows.api.Flow;
import org.opennms.netmgt.flows.api.FlowException;
import org.opennms.netmgt.flows.api.FlowRepository;
import org.opennms.netmgt.flows.api.FlowSource;
import org.opennms.netmgt.flows.classification.ClassificationEngine;
import org.opennms.netmgt.flows.classification.FilterService;
import org.opennms.netmgt.flows.classification.internal.DefaultClassificationEngine;
import org.opennms.netmgt.flows.classification.persistence.api.RuleBuilder;
import org.opennms.netmgt.flows.elastic.thresholding.FlowThresholding;
import org.opennms.netmgt.model.events.EventBuilder;
import org.opennms.netmgt.threshd.api.ThresholdingService;
import org.opennms.test.JUnitConfigurationEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.support.TransactionTemplate;

import com.codahale.metrics.MetricRegistry;
import com.google.common.collect.ImmutableList;

import io.searchbox.client.JestClient;

@RunWith(OpenNMSJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:/META-INF/opennms/applicationContext-soa.xml",
        "classpath:/META-INF/opennms/applicationContext-daemon.xml",
        "classpath:/META-INF/opennms/applicationContext-commonConfigs.xml",
        "classpath:/META-INF/opennms/applicationContext-dao.xml",
        "classpath:/META-INF/opennms/applicationContext-databasePopulator.xml",
        "classpath:/META-INF/opennms/applicationContext-shared.xml",
        "classpath:/META-INF/opennms/mockEventIpcManager.xml",
        "classpath:/META-INF/opennms/applicationContext-minimal-conf.xml",
        "classpath:/META-INF/opennms/applicationContext-thresholding.xml",
        "classpath:/META-INF/opennms/applicationContext-testPostgresBlobStore.xml",
        "classpath:/META-INF/opennms/applicationContext-testThresholdingDaos.xml",
        "classpath:/META-INF/opennms/applicationContext-testPollerConfigDaos.xml",
        "classpath:/META-INF/opennms/applicationContext-rpc-utils.xml",
})
@JUnitConfigurationEnvironment
@JUnitTemporaryDatabase(reuseDatabase = false, tempDbClass = MockDatabase.class)
public class ThresholdingIT {
    @Rule
    public final ElasticSearchRule elasticSearchRule = new ElasticSearchRule();

    @Autowired
    private ThresholdingService thresholdingService;

    @Autowired
    private DatabasePopulator databasePopulator;

    @Autowired
    private InterfaceToNodeCache interfaceToNodeCache;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private OverrideableThresholdingDao thresholdingDao;

    @Autowired
    private OverrideableThreshdDao threshdDao;

    @Autowired
    private MockEventIpcManager mockEventIpcManager;

    @Autowired
    private ApplicationContext applicationContext;

    @Before
    public void before() {
        this.applicationContext.getAutowireCapableBeanFactory().createBean(DefaultResourceTypeMapper.class);

        BeanUtils.assertAutowiring(this);

        this.databasePopulator.populateDatabase();
        this.interfaceToNodeCache.dataSourceSync();
    }

    private List<Flow> createMockedFlows(final int count) {
        final List<Flow> flows = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            final FlowDocument flowDocument = new FlowDocument();
            flowDocument.setIpProtocolVersion(4);
            flowDocument.setInputSnmp(10);
            flowDocument.setOutputSnmp(20);
            flowDocument.setSrcAddr(String.format("192.168.%d.%d", i % 256, 255 - (i % 256)));
            flowDocument.setDstAddr(String.format("192.168.%d.%d", 255 - (i % 256), i % 256));
            flowDocument.setSrcPort(1);
            flowDocument.setDstPort(2);
            flowDocument.setProtocol(6);
            flowDocument.setBytes(1024L);
            flowDocument.setDirection(Direction.INGRESS);

            final TestFlow flow = new TestFlow(flowDocument);
            flows.add(flow);
        }
        return flows;
    }

    private FlowRepository createFlowRepository(final JestClient jestClient) throws InterruptedException {
        this.thresholdingDao.overrideConfig(getClass().getResourceAsStream("/thresholds.xml"));
        this.threshdDao.overrideConfig(getClass().getResourceAsStream("/threshd-configuration.xml"));

        this.threshdDao.rebuildPackageIpListMap();

        final var metricRegistry = new MetricRegistry();
        final var sessionUtils = new MockSessionUtils();

        final ClassificationEngine classificationEngine = new DefaultClassificationEngine(() -> ImmutableList.<org.opennms.netmgt.flows.classification.persistence.api.Rule>builder()
                                                                                                             .add(new RuleBuilder().withName("APP1").withDstPort("1").withPosition(1).build())
                                                                                                             .add(new RuleBuilder().withName("APP2").withDstPort("2").withPosition(1).build())
                                                                                                             .build(), FilterService.NOOP);

        final var documentEnricher = new DocumentEnricher(metricRegistry,
                                                          this.databasePopulator.getNodeDao(),
                                                          this.interfaceToNodeCache,
                                                          sessionUtils,
                                                          classificationEngine,
                                                          new CacheConfigBuilder()
                                                                  .withName("flows.node")
                                                                  .withMaximumSize(1000)
                                                                  .withExpireAfterWrite(300)
                                                                  .build(), 0);

        final var nodeDao = new MockNodeDao();
        final var snmpInterfaceDao = new MockSnmpInterfaceDao();
        final var identity = new MockIdentity();
        final var tracerRegistry = new MockTracerRegistry();
        final var enrichedFlowForwarder = new MockDocumentForwarder();
        final var indexSettings = new IndexSettings();

        final var collectionAgentFactory = new DefaultCollectionAgentFactory();
        collectionAgentFactory.setNodeDao(this.databasePopulator.getNodeDao());
        collectionAgentFactory.setIpInterfaceDao(this.databasePopulator.getIpInterfaceDao());
        collectionAgentFactory.setPlatformTransactionManager(this.transactionTemplate.getTransactionManager());

        final var sessionCacheConfig = new CacheConfigBuilder()
                .withName("flow-thresholding-sessions")
                .build();
        final var thresholding = new FlowThresholding(this.thresholdingService,
                                                      collectionAgentFactory,
                                                      sessionCacheConfig);

        final var elasticFlowRepository = new ElasticFlowRepository(
                metricRegistry,
                jestClient,
                IndexStrategy.MONTHLY,
                documentEnricher,
                sessionUtils,
                nodeDao,
                snmpInterfaceDao,
                identity,
                tracerRegistry,
                enrichedFlowForwarder,
                indexSettings,
                thresholding);

        return new InitializingFlowRepository(elasticFlowRepository, jestClient);
    }

    @Test
    public void testThresholding() throws Exception {
        final RestClientFactory restClientFactory = new RestClientFactory(this.elasticSearchRule.getUrl());

        try (final JestClient jestClient = restClientFactory.createClient()) {
            final var eventAnticipator = this.mockEventIpcManager.getEventAnticipator();
            eventAnticipator.anticipateEvent(new EventBuilder(EventConstants.HIGH_THRESHOLD_EVENT_UEI, "Test")
                                                     .setNodeid(1)
                                                     .setInterface(addr("192.168.1.1"))
                                                     .setService(FlowThresholding.SERVICE_NAME)
                                                     .getEvent());
            eventAnticipator.anticipateEvent(new EventBuilder(EventConstants.HIGH_THRESHOLD_REARM_EVENT_UEI, "Test")
                                                     .setNodeid(1)
                                                     .setInterface(addr("192.168.1.1"))
                                                     .setService(FlowThresholding.SERVICE_NAME)
                                                     .getEvent());

            final var flowRepository = createFlowRepository(jestClient);

            final var source = new FlowSource(this.databasePopulator.getNode1().getLocation().getLocationName(),
                                              InetAddressUtils.str(this.databasePopulator.getNode1().getPrimaryInterface().getIpAddress()),
                                              null);

            assertEquals(0, eventAnticipator.getUnanticipatedEvents().size());

            this.transactionTemplate.execute((tx) -> {
                try {
                    flowRepository.persist(createMockedFlows(1000), source);
                } catch (FlowException e) {
                    throw new RuntimeException(e);
                }
                return null;
            });

            await().atMost(60, TimeUnit.SECONDS).until(eventAnticipator::getAnticipatedEvents, hasSize(0));

            eventAnticipator.verifyAnticipated();
        }
    }
}