/**
 *  PouchDB service 
 *  @author : saliya ruwan
 *  @description : data handle using pouchdb
 **/

angular.module('app.services').factory('PouchDBService', dbService);
dbService.$inject = [];

function dbService() {
    this.db = null;
    var services = {
        init: init,
        deleteDB: deleteDB,
        getDocById: getDocById,
        getAll: getAll,
        createDoc: createDoc,
        createDocPost: createDocPost,
        updateDoc: updateDoc,
        deleteDoc: deleteDoc,
        bulkDocs: bulkDocs,
        dbListener: dbListener,
        putAttachment: putAttachment,
        getAttachment: getAttachment,
        deleteAttachment: deleteAttachment,
        query: query,
        viewCleanup: viewCleanup
    };

    return services;

    /**
     *  initialize database
     **/
    function init(name, debug) {
        this.db = new PouchDB(name);
        if(debug){
            return PouchDB.debug.enable('*');
        }
        PouchDB.debug.disable();    
    };

    /**
     *  delete database
     **/
    function deleteDB() {
        this.db.destroy().then(function (response) {
            console.log(response);
        }).catch(function (err) {
            console.log(err);
        });
    };

    /**
     *  get document
     **/
    function getDocById(id, callback) {
        this.db.get(id, function (err, doc) {
            if (err) {
                return callback(false, err);
            }
            callback(true, response);
        });
    }

    /**
     *  get all documents 
     *  use startkey/endkey (options) to find all docs in a range
     **/
    function getAll(callback, options) {
        var docOptions = (options) ? options : {
            include_docs: true,
            attachments: true
        };
        this.db.allDocs(docOptions, function (err, response) {
            if (err) {
                callback(false, err);
                return console.log(err);
            }
            callback(true, response);
        });
    }

    /**
     *  create Doc 
     **/
    function createDoc(doc, callback) {
        this.db.put(doc, function (err, response) {
            if (!callback) return;
            if (err) {
                return callback(false, err);
            }
            callback(true, response);
        });
    }

    /**
     *  create doc with post 
     *  PouchDB auto-generate an _id
     **/
    function createDocPost(doc, callback) {
        this.db.post(doc, function (err, response) {
            if (!callback) return;
            if (err) {
                return callback(false, err);
            }
            callback(true, response);
        });
    }


    /**
     *  update doc 
     *  @param doc : { _id: 'mydoc', _rev: doc._rev, ... }
     **/
    function updateDoc(doc, callback) {
        this.db.put(doc, function (err, response) {
            if (!callback) return;
            if (err) {
                return callback(false, err);
            }
            callback(true, response);
        });
    }


    /**
     *  delete doc by id
     **/
    function deleteDoc(id, callback) {
        this.db.get(id, function (err, doc) {
            if (err) {
                return (callback) ? callback(false, err) : err;
            }
            this.db.remove(doc._id, doc._rev, function (err, response) {
                if (!callback) return;
                if (err) {
                    return callback(false, err);
                }
                callback(true, response);
            });
        });
    }

    /**
     *  Bulk create, update or delete docs 
     *    [{
     *      title    : 'Lisa Says',
     *      _deleted : true,
     *      _id      : "doc1",
     *      _rev     : "1-84abc2a942007bee7cf55007cba56198"
     *    }]
     **/
    function bulkDocs(docs, callback) {
        this.db.bulkDocs(docs, function (err, response) {
            if (!callback) return;
            if (err) {
                return callback(false, err);
            }
            callback(true, response);
        });
    }

    /**
     *  database on change events doc 
     **/
    function dbListener(onChange, onComplete, onError) {
        var changes = this.db.changes({
            since: 'now',
            live: true,
            include_docs: true
        }).on('change', onChange).on('complete', onComplete).on('error', onError);

        return changes;
    }

    /**
     *  Put attachment for a document
     *  currently support base64-encoded string as data
     **/
    function putAttachment(docId, attachmentId, data, callback) {
        this.db.putAttachment(docId, attachmentId, attachment, 'text/plain', function (err, res) {
            if (!callback) return;
            if (err) {
                return callback(false, err);
            }
            callback(true, response);
        });
    }

    /**
     *  Get attachment 
     **/
    function getAttachment(docId, attachmentId, callback) {
        this.db.getAttachment(docId, attachmentId, function (err, blobOrBuffer) {
            if (err) {
                return callback(false, err);
            }
            callback(true, blobOrBuffer);
        });
    }

    /**
     *  delete attachment 
     **/
    function deleteAttachment(docId, attachmentId, rev, callback) {
        this.db.removeAttachment(docId, attachmentId, rev, function (err, res) {
            if (!callback) return;
            if (err) {
                return callback(false, err);
            }
            callback(true, response);
        });
    }

    /**
     *  Query docs
     **/
    function query(fun, options, callback) {
        this.db.query(fun, options, function (err, result) {
            if (err) {
                return callback(false, err);
            }
            callback(true, result);
        });
    }

    /**
     *  View Cleanup
     **/
    function viewCleanup(fun, options, callback) {
        this.db.viewCleanup(function (err, result) {
            if (!callback) return;
            if (err) {
                return callback(false, err);
            }
            callback(true, result);
        });
    }
}