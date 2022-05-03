package contracts

import org.springframework.cloud.contract.spec.Contract

[
        Contract.make {
            request {
                url "/songs/1"
                method GET()
            }

            response {
                status OK()
                headers {
                    contentType applicationJson()
                }
                body(
                        "id": 1,
                        "name": "Song1",
                        "artist": "Artist1",
                        "album": "Album1",
                        "length": "1:23",
                        "resourceId": 123,
                        "year": "2022"
                )
            }
        },
        Contract.make {
            request {
                url "/songs?id=1"
                method DELETE()
            }

            response {
                status OK()
                headers {
                    contentType applicationJson()
                }
                body(
                        "ids": [1]
                )
            }
        }

]