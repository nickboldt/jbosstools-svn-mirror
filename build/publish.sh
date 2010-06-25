#!/bin/bash
# Hudson script used to publish Tycho-built p2 update sites

# NOTE: sources MUST be checked out into ${WORKSPACE}/sources 

# define target zip filename for inclusion in uberbuilder's bucky aggregator
SNAPNAME=${JOB_NAME}-Update-SNAPSHOT.zip

# cleanup from last time
rm -fr ${WORKSPACE}/site; mkdir -p ${WORKSPACE}/site/${JOB_NAME}

for z in ${WORKSPACE}/sources/site/target/site.zip ${WORKSPACE}/sources/site/target/site_assembly.zip; do
	if [[ -f $z ]]; then
		#echo "$z ..."
		# note the job name, build number, and build ID of the latest snapshot zip
		echo "JOB_NAME = ${JOB_NAME}" > ${WORKSPACE}/site/${JOB_NAME}/JOB_NAME.txt
		echo "BUILD_NUMBER = ${BUILD_NUMBER}" > ${WORKSPACE}/site/${JOB_NAME}/BUILD_NUMBER.txt
		echo "BUILD_ID = ${BUILD_ID}" > ${WORKSPACE}/site/${JOB_NAME}/BUILD_ID.txt
	
		# unzip into workspace for publishing as unpacked site
		unzip -u -o -q -d ${WORKSPACE}/site/${JOB_NAME}/ $z
	
		# copy into workspace for access by bucky aggregator (same name every time)
		rsync -aq $z ${WORKSPACE}/site/${SNAPNAME}
	fi
done

# if component zips exist, copy them too; first site.zip, then site_assembly.zip
for z in $(find ${WORKSPACE}/sources/*/site/target -type f -name "site*.zip" | sort -r); do 
	#if [[ -f $z ]]; then
		y=${z%%/site/target/*}; y=${y##*/}
		#echo "[$y] $z ..."
		# unzip into workspace for publishing as unpacked site
		mkdir -p ${WORKSPACE}/site/${JOB_NAME}/$y
		unzip -u -o -q -d ${WORKSPACE}/site/${JOB_NAME}/$y $z
		# copy into workspace for access by bucky aggregator (same name every time)
		rsync -aq $z ${WORKSPACE}/site/${JOB_NAME}/${y}-Update-SNAPSHOT.zip
	#fi
done

# if zips exist produced & renamed by ant script, copy them too
for z in $(find ${WORKSPACE} -maxdepth 5 -mindepth 3 -name "*Update*.zip"); do 
	#echo "$z ..."
	unzip -u -o -q -d ${WORKSPACE}/site/${JOB_NAME}/ $z
	rsync -aq $z ${WORKSPACE}/site/${SNAPNAME}
done

# publish to download.jboss.org
if [[ $DESTINATION == "" ]]; then DESTINATION="tools@filemgmt.jboss.org:/downloads_htdocs/tools/builds/nightly/3.2.helios"; fi
if [[ -d ${WORKSPACE}/site/${JOB_NAME} ]]; then
	rsync -arzq --delete ${WORKSPACE}/site/${JOB_NAME} $DESTINATION/
fi
if [[ -f ${WORKSPACE}/site/${SNAPNAME} ]]; then
	# publish snapshot zip
	rsync -arzq --delete ${WORKSPACE}/site/${SNAPNAME} $DESTINATION/
fi

# get full build log and filter out Maven test failures
wget -q http://hudson.qa.jboss.com/hudson/job/${JOB_NAME}/${BUILD_NUMBER}/consoleText -O ${WORKSPACE}/site/${JOB_NAME}/buildlog.txt
sed -ne "/<<< FAI/,9 p" -ne "/LURE/,9 p" ${WORKSPACE}/site/${JOB_NAME}/buildlog.txt | uniq > ${WORKSPACE}/site/${JOB_NAME}/fail_log.txt
sed -ne "/<<< ERR/,9 p" -ne "/ROR/,9 p"  ${WORKSPACE}/site/${JOB_NAME}/buildlog.txt | uniq > ${WORKSPACE}/site/${JOB_NAME}/errorlog.txt
rsync -arzq ${WORKSPACE}/site/${JOB_NAME}/buildlog.txt $DESTINATION/${JOB_NAME}/
rsync -arzq ${WORKSPACE}/site/${JOB_NAME}/fail_log.txt $DESTINATION/${JOB_NAME}/
rsync -arzq ${WORKSPACE}/site/${JOB_NAME}/errorlog.txt $DESTINATION/${JOB_NAME}/
