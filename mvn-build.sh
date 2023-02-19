#!/bin/bash
mvn clean install -DDB_HOST="localhost" \
                  -DDB_NAME="blogpost" \
                  -DDB_USERNAME="postgres" \
                  -DDB_PASSWORD="lZBvHSzZoMui1Z9" \
                  -DDB_PORT="5432" \
                  -DJWT_EXPIRATION="3600000" \
                  -DJWT_REFRESH_EXPIRATION="604800000" \
                  -DJWT_SECRET="3273357638792F423F4528472B4B6250655368566D597133743677397A24432646294A404D635166546A576E5A7234753778214125442A472D4B615064526755" \
                  -DMAIL_USERNAME="sudobarre@gmail.com" \
                  -DMAIL_PASSWORD="rephgzlkqbbopddh" \
                  -DMAIL_HOST="smtp.gmail.com"
