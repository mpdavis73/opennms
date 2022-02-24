import API from '@/services'
import { QueryParameters, SearchResult, VuexContext } from '@/types'
import { SZLRequest, VerticesAndEdges } from '@/types/topology'
import { Edges, Nodes } from 'v-network-graph'
import { State } from './state'

interface ContextWithState extends VuexContext {
  state: State
}

const parseVerticesAndEdges = (resp: VerticesAndEdges, context: VuexContext) => {
  const edges: Edges = {}
  const vertices: Nodes = {}

  for (const edge of resp.edges) {
    edges[edge.label] = { source: edge.source.id, target: edge.target.id }
  }

  for (const vertex of resp.vertices) {
    vertices[vertex.id] = { name: vertex.label }
  }

  context.commit('SAVE_NODE_EDGES', edges)
  context.commit('SAVE_NODE_VERTICIES', vertices)
}

const getVerticesAndEdges = async (context: VuexContext, queryParameters?: QueryParameters) => {
  const resp = await API.getVerticesAndEdges(queryParameters)
  if (resp) {
    parseVerticesAndEdges(resp, context)
  }
}

const setSemanticZoomLevel = (context: ContextWithState, SML: number) => {
  context.commit('SET_SEMANTIC_ZOOM_LEVEL', SML)
  context.dispatch('getTopologyDataByLevelAndFocus')
}

const addFocusedNodeIds = (context: ContextWithState, nodeIds: string[]) => {
  context.commit('ADD_FOCUSED_NODE_IDS', nodeIds)
  context.dispatch('getTopologyDataByLevelAndFocus')
}

const getTopologyDataByLevelAndFocus = async (context: ContextWithState) => {
  const SZLRequest: SZLRequest = { semanticZoomLevel: context.state.semanticZoomLevel, verticesInFocus: context.state.focusedNodeIds }
  const resp = await API.getTopologyDataByLevelAndFocus(SZLRequest)
  if (resp) {
    parseVerticesAndEdges(resp, context)
  }
}

const setSelectedView = (context: VuexContext, view: string) => {
  context.commit('SET_SELECTED_VIEW', view)
}

const openLeftDrawer = (context: VuexContext) => {
  context.commit('SET_LEFT_DRAWER_OPEN', true)
}

const closeLeftDrawer = (context: VuexContext) => {
  context.commit('SET_LEFT_DRAWER_OPEN', false)
}

const addContextNodeToFocus = (context: VuexContext, nodeId: string) => {
  context.commit('ADD_NODE_TO_FOCUS_IDS', nodeId)
  context.dispatch('getTopologyDataByLevelAndFocus')
}

const removeContextNodeFromFocus = (context: VuexContext, nodeId: string) => {
  context.commit('REMOVE_NODE_FROM_FOCUS_IDS', nodeId)
  context.dispatch('getTopologyDataByLevelAndFocus')
}

const setFocusedSearchBarNodes = (context: VuexContext, nodes: SearchResult[]) => {
  context.commit('SET_FOCUSED_SEARCH_BAR_NODES', nodes)
}

const addFocusedSearchBarNode = (context: VuexContext, node: SearchResult) => {
  context.commit('ADD_FOCUSED_SEARCH_BAR_NODE', node)
}

const removeFocusedSearchBarNode = (context: VuexContext, node: SearchResult) => {
  context.commit('REMOVE_FOCUSED_SEARCH_BAR_NODE', node)
}

export default {
  getVerticesAndEdges,
  setSemanticZoomLevel,
  setSelectedView,
  openLeftDrawer,
  closeLeftDrawer,
  addFocusedNodeIds,
  getTopologyDataByLevelAndFocus,
  addContextNodeToFocus,
  removeContextNodeFromFocus,
  setFocusedSearchBarNodes,
  addFocusedSearchBarNode,
  removeFocusedSearchBarNode
}