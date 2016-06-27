/**
 *  Attachment Service module
 *  @author: Saliya Ruwan
 *  @Service to handle file attach, detach, download, get attachment list from a custom object 
 *   and check file available in the local
 */

angular.module('app.services', [])

.directive('attachmentUpload', ['$ionicActionSheet', '$q', function ($ionicActionSheet, $q) {
    // TODO : implement attachment upload ui 
    // * options multiple file support and single file support 
    // * add image - 1. camera , 2. album
    // * upload progress and remove(detach) functions 
    // * image full screen view with inAppBrowser or native image viwer
    // * initialize - check local files, if does not exsits download and save file
    // * if exsits show image files and update image data items
    return {
        restrict: 'E',
        link: function (scope, element, attrs) {

        }
    };
}])

.directive('attachmentView', ['$ionicActionSheet', function ($ionicActionSheet) {
    // TODO : implement attachment view ui 
    // * initialize - check local, if it does not exsits download, save and view
    // * use inAppBrowser to view image in fullScreen mode
    return {
        restrict: 'E',
        link: function (scope, element, attrs) {

        }
    };
}])


/**
 * Attachment service - upload, download, detach and get attachment list for an object
 *
 **/
.service('$attachmentService', ['$http', '$cordovaFileTransfer', function ($http, $cordovaFileTransfer) {

    var basePath = 'https://platform-preprod.gtnexus.com/rest/310';
    var dataKey = '70213cec5933b3956c9a201e7c9c59b40423ce98';
    var authorization = '';

    var services = {
        init: init,
        upload: upload,
        detach: detach,
        download: download,
        getAttachments: getAttachments,
        isFileExists: isFileExists
    };

    return services;

    function init(path, auth, key) {
        basePath = path;
        dataKey = key;
        authorization = auth;
    }

    /** service function to upload an image 
     *  @usage : attachmentService.upload('$pollB1','1234322312', filePath, function(result){}, function(error){}, function(fileEntry){}, function(progress){})
     */
    function upload(obj, uid, fileURL, successCallback, errorCallback, moveSuccessCallback, progressCallback) {
        if (!dataKey && !authorization && !basePath) {
            throw "initialize the service - set authorization, dataKey and basePath";
        }
        var url = basePath + '/' + obj + '/' + uid + '/attach?dataKey=' + dataKey;

        var timestamp = new Date().getTime();
        var fileName = timestamp + ".jpg";
        window.resolveLocalFileSystemURL(fileURL, function (fileEntry) {
            window.resolveLocalFileSystemURL(cordova.file.dataDirectory, function (fileSystem2) {
                    fileEntry.copyTo(
                        fileSystem2,
                        fileName,
                        onCopySuccess,
                        errorCallback
                    );
                },
                errorCallback);


            function onCopySuccess(entry) {
                if (moveSuccessCallback) {
                    moveSuccessCallback(entry);
                }

                var options = {};
                options.fileKey = "file";
                options.fileName = fileName;
                options.mimeType = "image/jpg";
                options.params = {};

                options.headers = {
                    'Authorization': authorization,
                    'Content-Type': 'application/octet-stream',
                    'Access-Control-Allow-Origin': '*',
                    'Connection': "Keep-Alive",
                    'Content-Disposition': 'form-data; filename="' + fileName + '"'
                };
                $cordovaFileTransfer.upload(url, entry.nativeURL, options).then(successCallback, errorCallback, progressCallback);
            }
        }, errorCallback);

    }


    /**
     *  Detach file from an object
     *  @usage : attachmentService.detach('$pollB1', '5432423434', '4352354123', fingerprint, function(staus, data){});
     **/
    function detach(obj, uid, attachmentUid, fingerprint, callback) {
        if (!dataKey && !authorization && !basePath) {
            throw "initialize the service - set authorization, dataKey and basePath";
        }
        var url = basePath + obj + '/' + uid + '/detach/' + attachmentUid + '?datakey=' + dataKey;

        $http.post(url, {}, {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': authorization,
                    'Access-Control-Allow-Origin': '*',
                    'If-Match': fingerprint
                }
            })
            .success(function (data, status) {
                if (status == 202) {
                    callback(true, data);
                    return;
                }
                callback(false, "Status : " + status);
            })
            .error(function (data, status) {
                callback(false, "Network Error  Status : " + status);
                return;
            });
    }

    /**
     *  download file from the server
     *  @usage : attachmentService.download('45242234.jpg', '234435242', function(result){}, function(error){}, function(progress){})
     **/
    function download(name, attachmentUid, successCallback, errorCallback, progressCallback) {
        if (!dataKey && !authorization && !basePath) {
            throw "initialize the service - set authorization, dataKey and basePath";
        }
        var url = basePath + '/media/' + attachmentUid + '?datakey=' + dataKey;
        var targetPath = cordova.file.dataDirectory + name; //TODO check ? dataDirectory or documentsDirectory
        var trustHosts = true;
        var options = {};
        options.headers = {
            'Authorization': authorization,
            'Access-Control-Allow-Origin': '*'
        };
        
        $cordovaFileTransfer.download(url, targetPath, options, trustHosts)
            .then(successCallback, errorCallback, progressCallback);
    }


    /**
     *  get attachments
     *  @usage : attachmentService.getAttachments('$pollB1','324123442', function(staus, data){})
     **/
    function getAttachments(obj, uid, callback) {
        if (!dataKey && !authorization && !basePath) {
            throw "initialize the service - set authorization, dataKey and basePath";
        }
        var url = basePath + '/' + obj + '/' + uid + '/attachment?datakey=' + dataKey;

        $http.get(url, {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': authorization,
                    'Access-Control-Allow-Origin': '*'
                }
            })
            .success(function (data, status) {
                if (status == 200) {
                    callback(true, data);
                    return;
                }
                callback(false, "Status : " + status);
                return;
            })
            .error(function (data, status) {
                callback(false, "Network Error  Status : " + status);
                return;
            });
    }


    /**
     *  check file exist in l
     *  @usage : attachmentService.isFileExists(fileName)
     **/

    function isFileExists(fileName, callback) {
        var store = cordova.file.dataDirectory;
        var filePath = store + fileName;
        window.resolveLocalFileSystemURL(filePath, success, fail);

        function success() {
            callback(filePath);
        }

        function fail() {
            callback(false);
        }
    }

}])