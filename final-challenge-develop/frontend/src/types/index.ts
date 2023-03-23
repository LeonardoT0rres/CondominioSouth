export interface AssemblyValues {
  name: string;
  locale: string;
  obs: string;
  start: string;
  cardinality: string;
  id?: number | string | undefined;
  status?: string;
}

export interface ScheduleValues {
  name: string;
  description: string;
  scheduleOrder: string;
  id?: number | string;
  status?: string | undefined;
  positiveVotes?: number | undefined;
  negativeVotes?: number | undefined;
  endTime?: any | undefined;
}

export interface InvalidValues {
  invalidName: boolean;
  invalidDescription: boolean;
  invalidOrder: boolean;
  invalidLocale?: boolean;
  invalidStart?: boolean;
}

export interface AuthValues {
  email: string;
  password: string;
  name?: string;
  apartmentNumber?: string;
}

export interface vote{
  vote: string;
  id?: string;
}
