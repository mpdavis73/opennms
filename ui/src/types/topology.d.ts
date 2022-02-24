export interface VerticesAndEdges {
  vertices: ResponseVertex[]
  edges: ResponseEdge[]
}

// From response
export interface ResponseEdge {
  id: string
  label: string
  namespace: string
  source: {
    namespace: string
    id: string
  }
  target: {
    namespace: string
    id: string
  }
}

export interface ResponseVertex {
  iconKey: string
  id: string
  label: string
  namespace: string
  nodeID: string
  tooltipText: string
  x: string
  y: string
}

// For graph component
export interface NodePoint {
  x: number
  y: number
}

export interface SZLRequest {
  semanticZoomLevel: number
  verticesInFocus: string[]
}