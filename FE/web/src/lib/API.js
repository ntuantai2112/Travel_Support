
export const BASE_URL = "http://localhost:8888/qldv/api";
export const BASE_URL_DOWNLOAD = "http://localhost:8888/qldv/download?image=";
export const BASE_ANONYMOUS_URL = "http://localhost:8888/qldv";

const token = localStorage.getItem("token");

const API = {
    authorizedJSONPost: async (path, data) => {
        // const token = await AuthVerification.token()
        return (fetch(BASE_URL + path, {
            method: 'POST',
            headers: {
                Accept: "*/*",
                "Content-Type": "application/json",
                Authorization: "Bearer " + token
            },
            body: JSON.stringify(data)
        }).catch(e => {
            return { status: 500 }
        }));
    },
    authorizedJSONFavouritePost: async (path, data) => {
        // const token = await AuthVerification.token()
        return (fetch(BASE_ANONYMOUS_URL + path, {
            method: 'POST',
            headers: {
                Accept: "*/*",
                "Content-Type": "application/json",
                Authorization: "Bearer " + token
            },
            body: JSON.stringify(data)
        }).catch(e => {
            return { status: 500 }
        }));
    },
    authorizedJSONFavouriteGET: async (path) => {
        // const token = await AuthVerification.token()
    
        return (fetch(BASE_ANONYMOUS_URL + path, {
            method: "GET",
            headers: {
                Accept: "*/*",
                'Content-Type': 'application/x-www-form-urlencoded',
                Authorization: "Bearer " + token
            }
        }).catch(e => {
            return { status: 500 }
        }))
    },
    authorizedJSONBOOKPost: async (path, data,tokenBook) => {
        // const token = await AuthVerification.token()
        return (fetch(path, {
            method: 'POST',
            headers: {
                Accept: "*/*",
                "Content-Type": "application/json",
                Authorization: "Bearer " + tokenBook
            },
            body: JSON.stringify(data)
        }).catch(e => {
            return { status: 500 }
        }));
    },
    authorizedJSONPut: async (path, data) => {
        // const token = await AuthVerification.token()

        return (fetch(BASE_URL + path, {
            method: 'PUT',
            headers: {
                Accept: "*/*",
                "Content-Type": "application/json",
                Authorization: "Bearer " + token
            },
            body: JSON.stringify(data)
        }).catch(e => {
            return { status: 500 }
        }))
    },
    authorizedJSONDelete: async (path) => {
        // const token = await AuthVerification.token()

        return (fetch(BASE_URL + path, {
            method: 'DELETE',
            headers: {
                Accept: "*/*",
                "Content-Type": "application/x-www-form-urlencoded",
                Authorization: "Bearer " + token
            }
        }).catch(e => {
            return { status: 500 }
        }))
    },
    authorizedJSONGET: async (path) => {
        // const token = await AuthVerification.token()
    
        return (fetch(BASE_URL + path, {
            method: "GET",
            headers: {
                Accept: "*/*",
                'Content-Type': 'application/x-www-form-urlencoded',
                Authorization: "Bearer " + token
            }
        }).catch(e => {
            return { status: 500 }
        }))
    },
    authorizedJSONGETAVATAR: async (path,tokenParams) => {
        // const token = await AuthVerification.token()
      
        return (fetch(BASE_URL + path, {
            method: "GET",
            headers: {
                Accept: "*/*",
                'Content-Type': 'application/x-www-form-urlencoded',
                Authorization: "Bearer " + tokenParams
            }
        }).catch(e => {
            return { status: 500 }
        }))
    },
    authorizedFilePost: async (path, formData) => {
        // const token = await AuthVerification.token()

        return (fetch(BASE_URL + path, {
            method: 'POST',
            body: formData,
            headers: {
                // Accept: '*/*',
                // 'Content-Type': 'multipart/form-data',
                "Authorization": "Bearer " + token
            }
        }).catch(e => {
            return { status: 500 }
        }))
    },
    authorizedFilePut: async (path, formData) => {
        // const token = await AuthVerification.token()

        return (fetch(BASE_URL + path, {
            method: 'PUT',
            body: formData,
            headers: {
                // Accept: '*/*',
                // 'Content-Type': 'multipart/form-data',
                "Authorization": "Bearer " + token
            }
        }).catch(e => {
            return { status: 500 }
        }))
    },
    // firebaseFileUpload: async (uploadFile, name) => {
    //     return (new Promise((resolve, reject) => {
    //         const ext = uploadFile.name.split('.').pop()
    //         const filename = name + "." + ext
    //         const uploadTask = firebase.storage().ref().child(filename).put(uploadFile)
    //         uploadTask.on(firebase.storage.TaskEvent.STATE_CHANGED,
    //             function (snapshot) {
    //             }, function (error) {
    //                 reject(error)
    //             }, function () {
    //                 uploadTask.snapshot.ref.getDownloadURL().then(function (downloadURL) {
    //                     resolve(downloadURL)
    //                 });
    //             })
    //     }))
    // },
    // firebaseFileDelete: async (filename) => {
    //     return (firebase.storage().ref().child(filename).delete().catch(error => {
    //         return;
    //     }))
    // },
    anonymousJSONPost: async (path, data) => {
        return (fetch(BASE_URL + path, {
            method: 'POST',
            headers: {
                Accept: "*/*",
                "Content-Type": "application/json"
            },
            body: JSON.stringify(data)
        }).catch(error => {
            console.log(error);
            return { status: 500 }
        }));
    },
    anonymousJSONPOST: async (path, data) => {
        return (fetch(BASE_ANONYMOUS_URL + path, {
            method: 'POST',
            headers: {
                Accept: "*/*",
                "Content-Type": "application/json"
            },
            body: JSON.stringify(data)
        }).catch(error => {
            console.log(error);
            return { status: 500 }
        }));
    },
    anonymousJSONGET: async (path) => {
        return (fetch(BASE_URL + path, {
            method: "GET",
            headers: {
                Accept: "*/*",
                'Content-Type': 'application/json'
            }
        }).catch(e => {
            return { status: 500 }
        }))
    },
    anonymousJSONGETHOTEL: async (path) => {
        return (fetch(BASE_ANONYMOUS_URL + path, {
            method: "GET",
            headers: {
                Accept: "*/*",
                'Content-Type': 'application/json'
            }
        }).catch(e => {
            return { status: 500 }
        }))
    },
    anonymousJSONPut: async (path, data) => {
        return (fetch(BASE_URL + path, {
            method: 'PUT',
            headers: {
                Accept: "*/*",
                "Content-Type": "application/json"
            },
            body: JSON.stringify(data)
        }).catch(e => {
            return { status: 500 }
        }))
    },

    // anonymousFilePost: async (path, formData) => {
    //     const token = await AuthVerification.token()

    //     return (fetch(BASE_URL + path, {
    //         method: 'POST',
    //         body: formData,
    //         headers: {
    //             Accept: '*/*',
    //             'Content-Type': 'multipart/form-data',
    //             "Authorization": "Bearer " + token
    //         }
    //     }).catch(e => {
    //         return { status: 500 }
    //     }))
    // },
}

export default API;
