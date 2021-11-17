import { rest } from './axiosInstances'

const endpoint = '/logs'

const getLogs = async (): Promise<string[]> => {
  try {
    const resp = await rest.get(endpoint)
    return resp.data
  } catch (err) {
    return []
  }
}

const getLog = async (fileName: string, reverse: boolean = false): Promise<string> => {
  try {
    const resp = await rest.get(`${endpoint}/contents?f=${fileName}&reverse=${reverse}`)
    return resp.data
  } catch (err) {
    return ''
  }
}

export {
  getLogs,
  getLog
}
