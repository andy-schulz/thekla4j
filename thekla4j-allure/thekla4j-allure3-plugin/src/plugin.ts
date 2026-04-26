import type { AllureStore, Plugin, PluginContext } from "@allurereport/plugin-api";
import type { RequirementsPluginOptions, RequirementEntry } from "./model.js";

const REQUIREMENT_LINK_TYPE = "requirement";
const DEFAULT_FILE_NAME = "requirements.json";

export class RequirementsPlugin implements Plugin {
  readonly #options: Required<RequirementsPluginOptions>;

  constructor(options: RequirementsPluginOptions = {}) {
    this.#options = {
      fileName: options.fileName ?? DEFAULT_FILE_NAME,
    };
  }

  async done(context: PluginContext, store: AllureStore): Promise<void> {
    const results = await store.allTestResults();

    const byRequirement = new Map<string, RequirementEntry>();

    for (const result of results) {
      for (const link of result.links) {
        if (link.type !== REQUIREMENT_LINK_TYPE) continue;

        const key = link.url;
        if (!byRequirement.has(key)) {
          byRequirement.set(key, { id: link.name ?? link.url, url: link.url, tests: [] });
        }

        byRequirement.get(key)!.tests.push({
          name: result.name,
          status: result.status,
          flaky: result.flaky,
        });
      }
    }

    const data = Buffer.from(
      JSON.stringify([...byRequirement.values()], null, 2),
      "utf-8",
    );

    await context.reportFiles.addFile(this.#options.fileName, data);
  }
}
