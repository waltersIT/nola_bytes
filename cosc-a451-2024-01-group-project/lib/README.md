# "lib" subfolder

This folder holds the libraries needed to compile the Voting Machine.

Note that the main `.gitignore` file will cause new JAR files in this folder to be ignored and excluded from version control.  To add a new JAR file, e.g. `pgodbc.jar` so that the whole team gets it, you will need to `--force` it to be added:

```bash
git add --force lib/pgodbc.jar
```

