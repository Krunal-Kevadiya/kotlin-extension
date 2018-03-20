#!/usr/bin/env bash
BUILD_VARIANT_TYPE="$1"
VERSION_TYPE="$2"
BUNDLE_ID=""
TYPE_OF_OPERATION=2
echo -----------------------------------------------------------------------------
echo                          Applying project setting
echo -----------------------------------------------------------------------------
echo build variant type :- "$BUILD_VARIANT_TYPE"
echo version type :- "$VERSION_TYPE"
echo Bundle id is :- "$BUNDLE_ID"
echo Type of Operation :- "$TYPE_OF_OPERATION"
echo -----------------------------------------------------------------------------

if [ $TYPE_OF_OPERATION == 1 ] || [ $TYPE_OF_OPERATION == 3 ]; then
    echo                          Fetch Branding API Response Status
    echo -----------------------------------------------------------------------------
    HTTP_RESPONSE=$(curl -H "Content-type: application/json" -X POST -d '{"bundleId":"'$BUNDLE_ID'","apptype":"android"}' http://demo.simformsolutions.com:55370/api/Branding/GetBrandingForScript)

    echo API Response :- $HTTP_RESPONSE
    echo -----------------------------------------------------------------------------

    echo                          Get data from API Response Status
    echo -----------------------------------------------------------------------------
    PRIMARY_COLOR=$(echo $HTTP_RESPONSE  | python -c "import sys, json; print json.load(sys.stdin)['jsonData']['PrimaryColor']")
    echo Primary Color :- "$PRIMARY_COLOR"

    SECONDARY_COLOR=$(echo $HTTP_RESPONSE  | python -c "import sys, json; print json.load(sys.stdin)['jsonData']['SecondaryColor']")
    echo Primary Color :- "$SECONDARY_COLOR"

    APP_NAME=$(echo $HTTP_RESPONSE  | python -c "import sys, json; print json.load(sys.stdin)['jsonData']['ApplicationName']")
    echo Application Name :- "$APP_NAME"

    SKU_PREMIUM_MONTHLY=$(echo $HTTP_RESPONSE  | python -c "import sys, json; print json.load(sys.stdin)['jsonData']['InAPPId']")
    echo Sku Premium Monthly :- "$SKU_PREMIUM_MONTHLY"

    BASE64_ENCODED_PUBLICKEY=$(echo $HTTP_RESPONSE  | python -c "import sys, json; print json.load(sys.stdin)['jsonData']['InAppAndroidBase64Key']")
    echo Base64 Encoded PublicKey :- "$BASE64_ENCODED_PUBLICKEY"

    CRASHLYTICS_KEY='96c68e9c0138cb4916e862697ecd5746657596e6'
    echo Crashlytics Key :- "$CRASHLYTICS_KEY"

    PACKAGE_NAME=$(echo $HTTP_RESPONSE  | python -c "import sys, json; print json.load(sys.stdin)['jsonData']['Android']['package_name']")
    echo Package Name :- "$PACKAGE_NAME"

    MIPMAP_MDPI=$(echo $HTTP_RESPONSE  | python -c "import sys, json; print json.load(sys.stdin)['jsonData']['Android']['mipmap_mdpi']")
    echo App icon mdpi :- "$MIPMAP_MDPI"

    MIPMAP_HDPI=$(echo $HTTP_RESPONSE  | python -c "import sys, json; print json.load(sys.stdin)['jsonData']['Android']['mipmap_hdpi']")
    echo App icon hdpi :- "$MIPMAP_HDPI"

    MIPMAP_XHDPI=$(echo $HTTP_RESPONSE  | python -c "import sys, json; print json.load(sys.stdin)['jsonData']['Android']['mipmap_xhdpi']")
    echo App icon xhdpi :- "$MIPMAP_XHDPI"

    MIPMAP_XXHDPI=$(echo $HTTP_RESPONSE  | python -c "import sys, json; print json.load(sys.stdin)['jsonData']['Android']['mipmap_xxhdpi']")
    echo App icon xxhdpi :- "$MIPMAP_XXHDPI"

    MIPMAP_XXXHDPI=$(echo $HTTP_RESPONSE  | python -c "import sys, json; print json.load(sys.stdin)['jsonData']['Android']['mipmap_xxxhdpi']")
    echo App icon xxxhdpi :- "$MIPMAP_XXXHDPI"

    BANNER_LOGO=$(echo $HTTP_RESPONSE  | python -c "import sys, json; print json.load(sys.stdin)['jsonData']['Android']['android_banner_logo']")
    echo banner logo :- "$BANNER_LOGO"

    function checkImageURL {
        filename=$(basename "$1")
        extension="${filename##*.}"
        if [[ $filename =~ \.(jpg|png|gif|bmp|jpeg)$ ]]; then
            echo true
        else
            echo false
        fi
    }

    echo -----------------------------------------------------------------------------
    echo            Please wait while downloading project asset and change
    echo -----------------------------------------------------------------------------
    if [ ! -z "$MIPMAP_MDPI" -a "$MIPMAP_MDPI" != " " ] && [ $(checkImageURL "${MIPMAP_MDPI}") ]; then
        curl -o ../app/src/main/res/mipmap-mdpi/ic_launcher.png $MIPMAP_MDPI
    fi
    if [ ! -z "$MIPMAP_HDPI" -a "$MIPMAP_HDPI" != " " ] && [ $(checkImageURL "${MIPMAP_HDPI}") ]; then
        curl -o ../app/src/main/res/mipmap-hdpi/ic_launcher.png $MIPMAP_HDPI
    fi
    if [ ! -z "$MIPMAP_XHDPI" -a "$MIPMAP_XHDPI" != " " ] && [ $(checkImageURL "${MIPMAP_XHDPI}") ]; then
        curl -o ../app/src/main/res/mipmap-xhdpi/ic_launcher.png $MIPMAP_XHDPI
    fi
    if [ ! -z "$MIPMAP_XXHDPI" -a "$MIPMAP_XXHDPI" != " " ] && [ $(checkImageURL "${MIPMAP_XXHDPI}") ]; then
        curl -o ../app/src/main/res/mipmap-xxhdpi/ic_launcher.png $MIPMAP_XXHDPI
    fi
    if [ ! -z "$MIPMAP_XXXHDPI" -a "$MIPMAP_XXXHDPI" != " " ] && [ $(checkImageURL "${MIPMAP_XXXHDPI}") ]; then
        curl -o ../app/src/main/res/mipmap-xxxhdpi/ic_launcher.png $MIPMAP_XXXHDPI
    fi
    if [ ! -z "$BANNER_LOGO" -a "$BANNER_LOGO" != " " ] && [ $(checkImageURL "${BANNER_LOGO}") ]; then
        curl -o ../app/src/main/res/mipmap-hdpi/logo.png $BANNER_LOGO
    fi

    echo -----------------------------------------------------------------------------
    echo            Please wait while change application name and color
    echo -----------------------------------------------------------------------------
    #Change application name is strings.xml
    STRING_XML_PATH=../app/src/main/res/values/strings.xml
    TEMP_STRING_XML_PATH=${STRING_XML_PATH}.txt

    echo "strings file = ${STRING_XML_PATH}"
    if [ -f "${STRING_XML_PATH}" ]; then
        echo "strings xml exist!"
    else
        echo "strings xml not found!!"
        exit 1
    fi

    if [ ! -z "$APP_NAME" -a "$APP_NAME" != " " ]; then
        cat ${STRING_XML_PATH} | sed "s/<string name=\"app_name\">.*<\/string>/<string name=\"app_name\">${APP_NAME}<\/string>/" > ${TEMP_STRING_XML_PATH}
        cat ${TEMP_STRING_XML_PATH} | sed "s/APP_VERSION_NAME/${APP_NAME}/" > ${STRING_XML_PATH}
    fi
    if [ ! -z "$SKU_PREMIUM_MONTHLY" -a "$SKU_PREMIUM_MONTHLY" != " " ]; then
        cat ${STRING_XML_PATH} | sed "s/<string name=\"sku_premium_monthly\">.*<\/string>/<string name=\"sku_premium_monthly\">${SKU_PREMIUM_MONTHLY}<\/string>/" > ${TEMP_STRING_XML_PATH}
        cat ${TEMP_STRING_XML_PATH} | sed "s/APP_VERSION_NAME/${SKU_PREMIUM_MONTHLY}/" > ${STRING_XML_PATH}
    fi

    var="/"
    replace="\/"
    BASE64_ENCODED_PUBLICKEY=${BASE64_ENCODED_PUBLICKEY//$var/$replace}
    if [ ! -z "$BASE64_ENCODED_PUBLICKEY" -a "$BASE64_ENCODED_PUBLICKEY" != " " ]; then
        cat ${STRING_XML_PATH} | sed "s/<string name=\"base64_encoded_publickey\">.*<\/string>/<string name=\"base64_encoded_publickey\">${BASE64_ENCODED_PUBLICKEY}<\/string>/" > ${TEMP_STRING_XML_PATH}
        cat ${TEMP_STRING_XML_PATH} | sed "s/APP_VERSION_NAME/${BASE64_ENCODED_PUBLICKEY}/" > ${STRING_XML_PATH}
    fi
    rm -f ${TEMP_STRING_XML_PATH}

    #Change primary and secondary color in colors.xml
    COLOR_XML_PATH=../app/src/main/res/values/colors.xml
    TEMP_COLOR_XML_PATH=${COLOR_XML_PATH}.txt

    echo "colors file = ${COLOR_XML_PATH}"
    if [ -f "${COLOR_XML_PATH}" ]; then
        echo "colors xml exist!"
    else
        echo "colors xml not found!!"
        exit 1
    fi

    if [ ! -z "$PRIMARY_COLOR" -a "$PRIMARY_COLOR" != " " ]; then
        cat ${COLOR_XML_PATH} | sed "s/<color name=\"colorPrimary\">.*<\/color>/<color name=\"colorPrimary\">${PRIMARY_COLOR}<\/color>/" > ${TEMP_COLOR_XML_PATH}
        cat ${TEMP_COLOR_XML_PATH} | sed "s/APP_VERSION_NAME/${PRIMARY_COLOR}/" > ${COLOR_XML_PATH}
        cat ${COLOR_XML_PATH} | sed "s/<color name=\"colorPrimaryDark\">.*<\/color>/<color name=\"colorPrimaryDark\">${PRIMARY_COLOR}<\/color>/" > ${TEMP_COLOR_XML_PATH}
        cat ${TEMP_COLOR_XML_PATH} | sed "s/APP_VERSION_NAME/${PRIMARY_COLOR}/" > ${COLOR_XML_PATH}
    fi
    if [ ! -z "$SECONDARY_COLOR" -a "$SECONDARY_COLOR" != " " ]; then
        cat ${COLOR_XML_PATH} | sed "s/<color name=\"yellow_color\">.*<\/color>/<color name=\"yellow_color\">${SECONDARY_COLOR}<\/color>/" > ${TEMP_COLOR_XML_PATH}
        cat ${TEMP_COLOR_XML_PATH} | sed "s/APP_VERSION_NAME/${SECONDARY_COLOR}/" > ${COLOR_XML_PATH}
    fi
    rm -f ${TEMP_COLOR_XML_PATH}

    PROJECT_PROPERTY_FILE=../settings/versions/project.properties
    if [ -f "${PROJECT_PROPERTY_FILE}" ]; then
        echo "projectDetail properties exist!"

        function getProperty {
            grep $1 $2|cut -d'=' -f2
        }
        setProperty(){
          awk -v pat="^$1=" -v value="$1=$2" '{ if ($0 ~ pat) print value; else print $0; }' $3 > $3.tmp
          mv $3.tmp $3
        }

        if [ ! -z "$PACKAGE_NAME" -a "$PACKAGE_NAME" != " " ]; then
            setProperty "APPLICATION_ID" "${PACKAGE_NAME}" "${PROJECT_PROPERTY_FILE}"
        fi
        if [ ! -z "$CRASHLYTICS_KEY" -a "$CRASHLYTICS_KEY" != " " ]; then
            setProperty "CRASHLYTICS_KEY" "${CRASHLYTICS_KEY}" "${PROJECT_PROPERTY_FILE}"
        fi

        echo "Application Id - $(getProperty "APPLICATION_ID" "${PROJECT_PROPERTY_FILE}")"
        echo "Crashlytics Key - $(getProperty "CRASHLYTICS_KEY" "${PROJECT_PROPERTY_FILE}")"
    else
        echo "projectDetail properties not found!!"
        exit 1
    fi
    echo -----------------------------------------------------------------------------
fi

#Change application version name and code
if [ $TYPE_OF_OPERATION == 3 ] || [ $TYPE_OF_OPERATION == 2 ]; then
    case $BUILD_VARIANT_TYPE in
    	development)
            DEV_PROPERTY_FILE=../settings/versions/development.properties
    		if [ -f "${DEV_PROPERTY_FILE}" ]; then
                echo "development properties exist!"

                function getProperty {
                    grep $1 $2|cut -d'=' -f2
                }
                setProperty(){
                  awk -v pat="^$1=" -v value="$1=$2" '{ if ($0 ~ pat) print value; else print $0; }' $3 > $3.tmp
                  mv $3.tmp $3
                }

                VERSION_MAJOR=$(getProperty "VERSION_MAJOR" "${DEV_PROPERTY_FILE}")
                VERSION_MINOR=$(getProperty "VERSION_MINOR" "${DEV_PROPERTY_FILE}")
                VERSION_PATCH=$(getProperty "VERSION_PATCH" "${DEV_PROPERTY_FILE}")
                VERSION_CODE=$(getProperty "VERSION_CODE" "${DEV_PROPERTY_FILE}")

                case $VERSION_TYPE in
                        Major)
                                VERSION_MAJOR=$((VERSION_MAJOR+1))
                                VERSION_MINOR=0
                                VERSION_PATCH=0
                                VERSION_CODE=$((VERSION_CODE+1))
            		            ;;
            		    Minor)
            		            VERSION_MINOR=$((VERSION_MINOR+1))
                                VERSION_PATCH=0
                                VERSION_CODE=$((VERSION_CODE+1))
                                ;;
                        Patch)
                                VERSION_PATCH=$((VERSION_PATCH+1))
                                VERSION_CODE=$((VERSION_CODE+1))
                                ;;
                        Reset)
                                VERSION_MAJOR=1
                                VERSION_MINOR=0
                                VERSION_PATCH=0
                                VERSION_CODE=1
                                ;;
                        *)
                                VERSION_CODE=$((VERSION_CODE+1))
                                ;;
                esac

                setProperty "VERSION_MAJOR" "${VERSION_MAJOR}" "${DEV_PROPERTY_FILE}"
                setProperty "VERSION_MINOR" "${VERSION_MINOR}" "${DEV_PROPERTY_FILE}"
                setProperty "VERSION_PATCH" "${VERSION_PATCH}" "${DEV_PROPERTY_FILE}"
                setProperty "VERSION_CODE" "${VERSION_CODE}" "${DEV_PROPERTY_FILE}"

                echo "Version code - ${VERSION_CODE}"
                printf "Version Name - %d.%d.%d(%d)\n" "${VERSION_MAJOR}" "${VERSION_MINOR}" "${VERSION_PATCH}" "${VERSION_CODE}"
            else
                echo "development properties not found!!"
                exit 1
            fi
    		;;
    	qa)
            QA_PROPERTY_FILE=../settings/versions/qa.properties
            if [ -f "${QA_PROPERTY_FILE}" ]; then
                echo "qa properties exist!"

                function getProperty {
                    grep $1 $2|cut -d'=' -f2
                }
                setProperty(){
                  awk -v pat="^$1=" -v value="$1=$2" '{ if ($0 ~ pat) print value; else print $0; }' $3 > $3.tmp
                  mv $3.tmp $3
                }

                VERSION_MAJOR=$(getProperty "VERSION_MAJOR" "${QA_PROPERTY_FILE}")
                VERSION_MINOR=$(getProperty "VERSION_MINOR" "${QA_PROPERTY_FILE}")
                VERSION_PATCH=$(getProperty "VERSION_PATCH" "${QA_PROPERTY_FILE}")
                VERSION_CODE=$(getProperty "VERSION_CODE" "${QA_PROPERTY_FILE}")

                case $VERSION_TYPE in
                        Major)
                                VERSION_MAJOR=$((VERSION_MAJOR+1))
                                VERSION_MINOR=0
                                VERSION_PATCH=0
                                VERSION_CODE=$((VERSION_CODE+1))
                                ;;
                        Minor)
                                VERSION_MINOR=$((VERSION_MINOR+1))
                                VERSION_PATCH=0
                                VERSION_CODE=$((VERSION_CODE+1))
                                ;;
                        Patch)
                                VERSION_PATCH=$((VERSION_PATCH+1))
                                VERSION_CODE=$((VERSION_CODE+1))
                                ;;
                        Reset)
                                VERSION_MAJOR=1
                                VERSION_MINOR=0
                                VERSION_PATCH=0
                                VERSION_CODE=1
                                ;;
                        *)
                                VERSION_CODE=$((VERSION_CODE+1))
                                ;;
                esac

                setProperty "VERSION_MAJOR" "${VERSION_MAJOR}" "${QA_PROPERTY_FILE}"
                setProperty "VERSION_MINOR" "${VERSION_MINOR}" "${QA_PROPERTY_FILE}"
                setProperty "VERSION_PATCH" "${VERSION_PATCH}" "${QA_PROPERTY_FILE}"
                setProperty "VERSION_CODE" "${VERSION_CODE}" "${QA_PROPERTY_FILE}"

                echo "Version code - ${VERSION_CODE}"
                printf "Version Name - %d.%d.%d(%d)\n" "${VERSION_MAJOR}" "${VERSION_MINOR}" "${VERSION_PATCH}" "${VERSION_CODE}"
            else
                echo "qa properties not found!!"
                exit 1
            fi
    		;;
        production)
            PROD_PROPERTY_FILE=../settings/versions/production.properties
            if [ -f "${PROD_PROPERTY_FILE}" ]; then
                echo "production properties exist!"

                function getProperty {
                    grep $1 $2|cut -d'=' -f2
                }
                setProperty(){
                  awk -v pat="^$1=" -v value="$1=$2" '{ if ($0 ~ pat) print value; else print $0; }' $3 > $3.tmp
                  mv $3.tmp $3
                }

                VERSION_MAJOR=$(getProperty "VERSION_MAJOR" "${PROD_PROPERTY_FILE}")
                VERSION_MINOR=$(getProperty "VERSION_MINOR" "${PROD_PROPERTY_FILE}")
                VERSION_PATCH=$(getProperty "VERSION_PATCH" "${PROD_PROPERTY_FILE}")
                VERSION_CODE=$(getProperty "VERSION_CODE" "${PROD_PROPERTY_FILE}")

                case $VERSION_TYPE in
                        Major)
                                VERSION_MAJOR=$((VERSION_MAJOR+1))
                                VERSION_MINOR=0
                                VERSION_PATCH=0
                                VERSION_CODE=$((VERSION_CODE+1))
                                ;;
                        Minor)
                                VERSION_MINOR=$((VERSION_MINOR+1))
                                VERSION_PATCH=0
                                VERSION_CODE=$((VERSION_CODE+1))
                                ;;
                        Patch)
                                VERSION_PATCH=$((VERSION_PATCH+1))
                                VERSION_CODE=$((VERSION_CODE+1))
                                ;;
                        Reset)
                                VERSION_MAJOR=1
                                VERSION_MINOR=0
                                VERSION_PATCH=0
                                VERSION_CODE=1
                                ;;
                        *)
                                VERSION_CODE=$((VERSION_CODE+1))
                                ;;
                esac

                setProperty "VERSION_MAJOR" "${VERSION_MAJOR}" "${PROD_PROPERTY_FILE}"
                setProperty "VERSION_MINOR" "${VERSION_MINOR}" "${PROD_PROPERTY_FILE}"
                setProperty "VERSION_PATCH" "${VERSION_PATCH}" "${PROD_PROPERTY_FILE}"
                setProperty "VERSION_CODE" "${VERSION_CODE}" "${PROD_PROPERTY_FILE}"

                echo "Version code - ${VERSION_CODE}"
                printf "Version Name - %d.%d.%d(%d)\n" "${VERSION_MAJOR}" "${VERSION_MINOR}" "${VERSION_PATCH}" "${VERSION_CODE}"
            else
                echo "production properties not found!!"
                exit 1
            fi
            ;;
    	*)
    		echo "Sorry, I don't understand"
    		;;
    esac

    #Create keystore file if not exit.
    KEYSTORE_FILE=../settings/keystore/release.jks
    if [ -f "${KEYSTORE_FILE}" ]; then
        echo "keystore exist!"
    else
        echo "keystore not found!!"
        keytool -genkey -alias androidreleasekey -keyalg RSA -keystore $KEYSTORE_FILE -dname "cn=localhost, ou=IT, o=Continuent, c=US" -storepass password -keypass password
    fi
    echo -----------------------------------------------------------------------------
fi