# Contribution

## Development Setup

### Line Endings

This project uses **LF (Unix-style)** line endings for all text files to ensure consistency across Windows, Linux, and macOS.

**Configuration files:**
- `.gitattributes` - Git automatically normalizes line endings to LF
- `.editorconfig` - Your IDE (IntelliJ IDEA, VS Code, etc.) will use LF by default

**IntelliJ IDEA:**
- EditorConfig support is enabled by default
- To manually set: File > Line Separators > LF - Unix and macOS (\n)

**VS Code:**
- Install "EditorConfig for VS Code" extension
- Settings will be applied automatically

**Note:** If you're on Windows and see Git showing all files as modified after a fresh clone, run:
```bash
git add --renormalize .
git status
```

This is a one-time operation to normalize any existing files with CRLF to LF.

## Create release note

```bash
git log <TAG1>...<TAG2> --pretty="%h - %s"
```

## Setup publish plugin
