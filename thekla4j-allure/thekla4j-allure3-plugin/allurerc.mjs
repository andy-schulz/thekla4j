import { defineConfig } from "allure";

export default defineConfig({
  name: "Allure Report",
  output: "./allure-report",
  plugins: {
    requirements: {
      import: "@teststeps/thekla4j-allure3-plugin"
    }
  }
});
