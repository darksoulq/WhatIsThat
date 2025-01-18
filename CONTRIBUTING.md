# Contribution Guidelines

Thank you for your interest in contributing to this project! Follow these guidelines to ensure a smooth and productive collaboration.

---

## How to Contribute

1. **Fork the Repository**
   - Click the "Fork" button at the top right of this page to create a copy of the repository in your GitHub account.

2. **Clone Your Fork**
   - Clone your fork to your local machine using:
     ```bash
     git clone https://github.com/your-username/repo-name.git
     cd repo-name
     ```

3. **Set Upstream Remote**
   - Set the upstream to the main repository to keep your fork updated:
     ```bash
     git remote add upstream https://github.com/darksoulq/WhatIsThat.git
     ```

4. **Create a Branch**
   - Create a new branch for your feature or bug fix:
     ```bash
     git checkout -b feature/short-description
     ```

5. **Make Changes**
   - Make your changes in the codebase. Follow the existing code style and structure.
   - If adding a new feature, include relevant documentation and tests.

6. **Commit Your Changes**
   - Use descriptive commit messages:
     ```bash
     git add .
     git commit -m "Add a brief description of the changes"
     ```

7. **Push Your Changes**
   - Push your branch to your fork:
     ```bash
     git push origin feature/short-description
     ```

8. **Create a Pull Request**
   - Go to the main repository and click the "Compare & pull request" button.
   - Fill out the pull request details and submit the PR.

---

## Code Style

- Follow the existing coding standards in the repository.
- Use meaningful variable and function names.
- Keep commits focused—avoid including unrelated changes.

---

## Reporting Issues

If you encounter a bug or have a feature request, please [create an issue](https://github.com/darksoulq/WhatIsThat/issues) and include:
- A clear and concise title.
- A detailed description of the problem or feature request.
- Steps to reproduce the issue (if applicable).

---

## Additional Notes

- Contributions will be reviewed before merging. Be patient while maintainers review your changes.
- Ensure your branch is up-to-date with the `main` branch before submitting your pull request:
  ```bash
  git fetch upstream
  git checkout main
  git merge upstream/main
