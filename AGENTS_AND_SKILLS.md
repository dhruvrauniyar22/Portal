# Custom Agent and Skill

## Gemini Hostel Assistant

`GeminiHostelAgent` is the project custom agent. It assists students with hostel-related questions and complaint text.

- **Inputs:** a student question or informal complaint description.
- **Outputs:** concise hostel guidance, or a complaint-oriented response. It does not represent a completed staff action.
- **Workflow:** `StudentDashboard` sends chat questions to `GeminiHostelAgent.answerStudentQuestion`. The agent applies student-focused safety instructions and delegates to `GeminiService`. `GeminiService` is the only Gemini HTTP client.
- **Safety:** it must not invent policies, emergency instructions, room details, dates, or completed actions. It tells the student when staff confirmation is needed. A missing configuration, network error, HTTP error, or malformed response is returned as a safe message.
- **Implementation:** `APP/GeminiHostelAgent.java`, connected in `APP/StudentDashboard.java`; remote communication is implemented by `APP/GeminiService.java`.

## Complaint Summarization

`ComplaintSummarizationSkill` turns an informal complaint into a compact complaint draft.

- **Input:** non-empty student complaint text.
- **Output:** three labeled fields—`Category`, `Issue`, and `Priority`—generated from the supplied text only.
- **Workflow:** the student enters a description in the Submit Complaint screen and selects **Summarize complaint**. `StudentDashboard` calls `ComplaintSummarizationSkill.summarize`, which delegates through `GeminiHostelAgent` to `GeminiService`; it does not duplicate API logic.
- **Example:** “The bathroom tap has been leaking for two days and water is getting wasted.” can be summarized as `Category: Plumbing`, `Issue: Bathroom tap is leaking continuously.`, `Priority: Medium`.
- **Failure behavior:** blank input prompts the student to enter a description. Missing runtime configuration, network failures, and API response failures remain user-visible safe error messages; no fabricated summary is substituted.
- **Security:** only runtime environment configuration reaches `GeminiService`; the skill does not store or log API credentials or complaint text.
- **Implementation:** `APP/ComplaintSummarizationSkill.java`, integrated in `APP/StudentDashboard.java`.
