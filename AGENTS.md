# Development Constitution

- Preserve the existing Swing application, screens, and working user flows. Make focused changes instead of replacing the portal with a sample application.
- Keep credentials and tokens out of source code, documentation, tests, logs, and workflows. Gemini configuration is read only from the `GEMINI_API_KEY` environment variable at runtime.
- Do not commit compiled classes, build directories, IDE caches, or local environment files.
- Keep all Gemini HTTP communication in `APP/GeminiService.java`. Agents and skills must call that service instead of creating another API client.
- Prefer small, testable changes and keep the project compatible with Java 17.
- Do not claim functionality that the current UI or code does not implement. Keep sample dashboard data clearly separate from live integrations.
- Update `ARCHITECTURE.md`, `README.md`, and `AGENTS_AND_SKILLS.md` when implementation architecture, agents, or skills change.
- Compile the complete application and run available local validation before declaring work complete. CI must build without a Gemini key or network call.
