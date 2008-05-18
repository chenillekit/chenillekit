import org.apache.commons.net.ftp.FTP
import org.apache.commons.net.ftp.FTPClient
import org.apache.commons.net.ftp.FTPReply

/**
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 *
 * Copyright 1996-2008 by Sven Homburg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */


site_directories = ["main": "target/site", "t5c-commons": "commons/target/site", "t5c-contrib": "contrib/target/site"]
config_file = new File((String) System.properties['user.home'] + '/.chenillekit.properties')

if (!config_file.exists())
{
    println "config file $config_file.absolutePath not found!"
    System.exit(1)
}

properties = new Properties()
properties.load(config_file.newInputStream())

ftp_server = properties['ftp_server']
ftp_user = properties['ftp_user']
ftp_passwd = properties['ftp_passwd']
ftp_basedir = properties['ftp_basedir']

ftp = new FTPClient()
ftp.connect(ftp_server)
println ftp.replyString

ftp.login(ftp_user, ftp_passwd)
println "$ftp.replyString $ftp.replyCode"
if (ftp.replyCode != 230)
{
    ftp.disconnect()
    System.exit(1)
}

/**
 * change the working dir
 */
ftp.changeWorkingDirectory((String) ftp_basedir)
println ftp.replyString

/**
 * create all needed directories
 */
site_directories.each() {key, value ->

    if (key != 'main')
        ftp.mkd(key)

    new File((String) value).eachDirRecurse {File directory ->
        prefix = (key != 'main' ? "$key/" : '')
        ftpDir = directory.getPath().replace('\\', '/').replace("$value/", "$prefix")
        ftp.mkd(ftpDir)
        if (!FTPReply.isPositiveCompletion(ftp.replyCode))
            print "ERROR: $ftpDir - $ftp.replyString"
    }
}

/**
 * upload all files
 */
site_directories.each() {key, value ->

    new File((String) value).eachFileRecurse {File file ->
        if (!file.directory)
        {
            prefix = (key != 'main' ? "$key/" : '')
            ftpFile = file.getPath().replace('\\', '/').replace("$value/", "$prefix")
            ftp.setFileType(FTP.IMAGE_FILE_TYPE)
            ftp.storeFile(ftpFile, file.newInputStream())
            if (!FTPReply.isPositiveCompletion(ftp.replyCode))
                print "ERROR: $ftpFile - $ftp.replyString"
        }
    }
}

/**
 * Logout from the FTP Server and disconnect
 */
ftp.logout()
println ftp.replyString

ftp.disconnect()
