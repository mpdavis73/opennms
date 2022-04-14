import { QueryParameters } from '@/types'
import { SZLRequest, TopologyGraphList, VerticesAndEdges } from '@/types/topology'
import { queryParametersHandler } from './serviceHelpers'
import { v2 } from './axiosInstances'

const endpoint = '/graphs/nodes/nodes'
const graphs = '/graphs'

const getVerticesAndEdges = async (queryParameters?: QueryParameters): Promise<VerticesAndEdges | false> => {
  let endpointWithQueryString = ''

  if (queryParameters) {
    endpointWithQueryString = queryParametersHandler(queryParameters, endpoint)
  }

  try {
    const resp = await v2.get(endpointWithQueryString || endpoint)

    // no content from server
    if (resp.status === 204) {
      return { vertices: [], edges: [], defaultFocus: { type: '', vertexIds: [{} as any] } }
    }

    return resp.data
  } catch (err) {
    return false
  }
}

const getNodesTopologyDataByLevelAndFocus = async (payload: SZLRequest): Promise<VerticesAndEdges | false> => {
  try {
    const resp = await v2.post(endpoint, payload)

    // no content from server
    if (resp.status === 204) {
      return { vertices: [], edges: [], focus: { semanticZoomLevel: 1, vertices: [] } }
    }

    return resp.data
  } catch (error) {
    return false
  }
}

const getTopologyGraphs = async (): Promise<TopologyGraphList[]> => {
  try {
    const resp = await v2.get(graphs)
    return resp.data
  } catch (error) {
    return []
  }
}

const getTopologyGraphByContainerAndNamespace = async (
  containerId: string,
  namespace: string
): Promise<VerticesAndEdges | false> => {
  try {
    const resp = await v2.get(`graphs/${containerId}/${namespace}`)
    return resp.data
  } catch (error) {
    return false
  }
}

const getPowerGridTopologyDataByLevelAndFocus = async (
  containerId: string,
  namespace: string,
  payload: SZLRequest
): Promise<VerticesAndEdges | false> => {
  try {
    const resp = await v2.post(`graphs/${containerId}/${namespace}`, payload)

    // no content from server
    if (resp.status === 204) {
      return { vertices: [], edges: [], focus: { semanticZoomLevel: 1, vertices: [] } }
    }

    return resp.data
  } catch (error) {
    return false
  }
}

export {
  getVerticesAndEdges,
  getNodesTopologyDataByLevelAndFocus,
  getPowerGridTopologyDataByLevelAndFocus,
  getTopologyGraphs,
  getTopologyGraphByContainerAndNamespace
}