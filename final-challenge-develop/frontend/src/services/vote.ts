import { ScheduleValues, vote } from "../types";
import UseApiMock from './UseApiMock';

export function votePost(idSchedule: number | string | undefined, vote: vote) {
  return UseApiMock.post(`/votes/${idSchedule}/${1}`, vote);
}
