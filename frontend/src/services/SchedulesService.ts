import { ScheduleValues } from '../types';
import UseApiMock from './UseApiMock';

export function scheduleGet(id: string | undefined) {
  return UseApiMock.get(`/schedules/assembly/${id}`);
}

export function vaiGet(){
  UseApiMock.get("")
}

export function scheduleGetUni(idAssembly: string | undefined, idSchedule: string | undefined) {
  return UseApiMock.get(`/schedules/${idAssembly}/${idSchedule}`);
}

export function schedulePost(data: ScheduleValues, id: string | undefined) {
  return UseApiMock.post(`/schedules/${id}`, data);
}

export function schedulePut(data: ScheduleValues) {
  return UseApiMock.put(`/schedules/${data.id}`, data);
}

export function schedulePatch(id: string | number | undefined, time: any) {
  return UseApiMock.patch(`/schedules/start/${id}`,{startTime:time});
}

export function scheduleDelete(id: number | string | undefined) {
  return UseApiMock.deleteData(`/schedules/${id}`);
}
