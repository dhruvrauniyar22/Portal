# Java Hostel Complaint Portal

A Java Swing desktop portal with separate student and warden dashboards, complaint-oriented UI, notices, feedback, and optional Gemini-powered assistance.

## Requirements

- JDK 17 or later
- A graphical desktop to run the Swing UI

## Setup, build, and run

```powershell
Set-Location 'Hostel Compalin'
javac --release 17 -d build APP\*.java
java -cp build Login
```

The launch command must run from `Hostel Compalin` because the login screen uses relative image assets in `Icon`.

## Gemini configuration

Gemini is optional; compilation, local validation, and initial UI startup do not call the API. Set the key only in your current environment before starting the app.

Windows PowerShell:

```powershell
$env:GEMINI_API_KEY="YOUR_API_KEY"
```

Linux/macOS:

```bash
export GEMINI_API_KEY="YOUR_API_KEY"
```

The project uses Java `HttpURLConnection` in `APP/GeminiService.java`; it does not require Gson. Keep keys out of source, documentation, commit history, and CI configuration.

## Architecture, agent, and skill

See [ARCHITECTURE.md](ARCHITECTURE.md) for the actual component design and limitations. The student-facing **Gemini Hostel Assistant** is implemented in `APP/GeminiHostelAgent.java`. The in-form **Complaint Summarization** skill is implemented in `APP/ComplaintSummarizationSkill.java`. Their workflow and safety rules are documented in [AGENTS_AND_SKILLS.md](AGENTS_AND_SKILLS.md).

## CI/CD

GitHub Actions at `.github/workflows/build.yml` compiles every application source file with Java 17 and runs a non-network local validation on pushes and pull requests to `main`. It deliberately does not receive a Gemini key.

## Security

Use `GEMINI_API_KEY` only as a runtime environment variable. Generated class files, local build output, IDE settings, and local environment files are ignored by Git. Project development rules are in [AGENTS.md](AGENTS.md).
