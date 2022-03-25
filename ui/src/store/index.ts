import { createStore } from 'vuex'

// store modules
import searchModule from './search'
import nodesModule from './nodes'
import eventsModule from './events'
import ifServicesModule from './ifServices'
import spinnerModule from './spinner'
import inventoryModule from './inventory'
import locationsModule from './locations'
import mapModule from './map'
import fileEditorModule from './fileEditor'
import authModule from './auth'
import logsModule from './logs'
import appModule from './app'
import infoModule from './info'
import helpModule from './help'
import notificationModule from './notification'
import resourceModule from './resource'
import graphModule from './graph'
import topologyModule from './topology'
import pluginModule from './plugin'

export default createStore({
  modules: {
    searchModule,
    nodesModule,
    eventsModule,
    ifServicesModule,
    spinnerModule,
    inventoryModule,
    locationsModule,
    mapModule,
    fileEditorModule,
    logsModule,
    authModule,
    appModule,
    infoModule,
    helpModule,
    notificationModule,
    resourceModule,
    graphModule,
    topologyModule,
    pluginModule
  }
})
