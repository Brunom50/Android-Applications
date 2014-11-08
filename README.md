Android-Applications
====================
A collection of cleanly compiling Android apps (non-malware) for SDK 15 and JRE 1.6

Note to contributors: This is a public repo, please don't put private analysis files in this repo.

- Each folder in collection contains a unique project that consists of one or more Eclipse project files (if there are dependencies).
- All apps are set to compile for JDK 1.6 Compliance
- Each application is tested and compiled to SDK 15
- Applications may be renamed to make naming unique/consistent/etc.
- Source is unversioned from any version control system
- Removed any unit testing or test code
- Added missing dependency libraries when needed
- Minor fixes to make code compile if broken
- All other source is unchanged
- Added source.txt at root project folder with links to original source
- Added description.txt at root project folder with snippets of project descriptions
- Added sloc.csv at root project folder with results of Cloc SLOC counter (command below).

`cloc-1.56.exe --ignore-whitespace --quiet --csv --report-file="<OUTPUT FILE>" "<INPUT DIRECTORY>"`
