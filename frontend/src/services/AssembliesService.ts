import { AssemblyValues } from "../types";
import UseApiMock from './UseApiMock';

export function assemblyGet() {
  return UseApiMock.get("/assemblies")
}

export function assemblyGetUni(id:string | undefined) {
  return UseApiMock.get(`/assemblies/${id}`)
}

export function assemblyPatch(id: string | number | undefined, time: any) {
  return UseApiMock.patch(`/assemblies/end/${id}`, {startTime:time});
}

export function assemblyPost(data: AssemblyValues) {
  return UseApiMock.post("/assemblies", data);
}

export function assemblyPut(data: AssemblyValues) {
  return UseApiMock.put(`/assemblies/${data.id}`, data)
}

export function assemblyDelete(id: number | string | undefined) {
  return UseApiMock.deleteData(`/assemblies/${id}`)
}
  
// lista de endpoints http://localhost:8080/swagger-ui/index.html#/
// http://localhost:8080/assembly
// url pra testes https://6387a14cd9b24b1be3f61500.mockapi.io/api/assembly
