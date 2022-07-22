#!/bin/bash
curl -i -u personia:Edo123 http://0.0.0.0:1912/employees/Jonas/supervisors
echo ""
exit
curl -i http://0.0.0.0:1912/relations \
    -H "Content-Type: application/json"\
    -d '
{
    "Pete": "Nick",
    "Barbara": "Nick",
    "Sophie": "Jonas",
    "Nah": "Robin"
}
    '
echo ""
exit
curl -i http://0.0.0.0:1912/relations/

echo ""
exit
curl -i http://0.0.0.0:1912/relations \
    -H "Content-Type: application/json"\
    -d '
{
    "Pete": "Nick",
    "Barbara": "Nick",
    "Nick": "Sophie",
    "Sophie": "Jonas"
}
    '

echo ""
