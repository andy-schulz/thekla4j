export interface RequirementsPluginOptions {
  fileName?: string;
}

export interface RequirementEntry {
  id: string;
  url: string;
  tests: RequirementTestSummary[];
}

export interface RequirementTestSummary {
  name: string;
  status: string;
  flaky: boolean;
}
