import { AssemblyValues, AuthValues, ScheduleValues } from '../types';

export function lengthIsValid(data: string, length: number) {
  return data.length > length;
}
// name 3, local 8 e desc 20
export function itemOrderIsValid(data: string) {
  return +data > 0;
}

export function dateisValid(data: string) {
  const currentYear = new Date().getFullYear();

  const isNotPast = Date.parse(data) > Date.now();

  const currentNextYearString = (new Date().getFullYear() + 2).toString();

  const closeFuture = Date.parse(data) <= Date.parse(currentNextYearString);

  return isNotPast && closeFuture;
}

export function scheduleDataIsValid(data: ScheduleValues) {
  return (
    lengthIsValid(data.name, 3) &&
    lengthIsValid(data.description, 20) &&
    itemOrderIsValid(data.scheduleOrder)
  );
}

export function assemblyDataIsValid(data: AssemblyValues) {
  return (
    lengthIsValid(data.name, 3) &&
    lengthIsValid(data.locale, 8) &&
    lengthIsValid(data.obs, 20) &&
    dateisValid(data.start) &&
    itemOrderIsValid(data.cardinality)
  );
}

