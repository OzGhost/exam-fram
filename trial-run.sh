#!/bin/bash
curl -i http://0.0.0.0:1901/relations \
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
