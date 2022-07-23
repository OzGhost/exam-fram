
# Installation Guide
## Notice
The instruction was design for Linux, if you try it on Window or MacOS then it may not work.

## Pre-condition
- Docker
- OpenJDK 11 (or any alternative)
- Internet

## Steps
- Execute the file install.sh with your choosen password
```
./install.sh <passwd>
```
- Start the application
```
docker start mun-db && sleep 5 && docker start exam-fram-app
```
- Test the application with curl (example below)
```
#import relations
curl -i -u personia:<passwd> http://localhost:1912/relations/ \
    -H "Content-Type: application/json" \
    -d '
{
    "Pete": "Nick",
    "Barbara": "Nick",
    "Nick": "Sophie",
    "Sophie": "Jonas"
}'

#fetch hierarchy
curl -i -u personia:<passwd> http://localhost:1912/relations/

#query supervisors
curl -i -u personia:<passwd> http://localhost:1912/employees/Nick/supervisors
```
