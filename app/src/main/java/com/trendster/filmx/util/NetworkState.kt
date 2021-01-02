package com.trendster.filmx.util

/**  8f92a0db-2f55-446e-8f59-b159cf615418 */

enum class Status {
    RUNNING,
    SUCCESS,
    FAILED
}

class NetworkState (val status: Status , val msg : String) {

    companion object{

        val LOADED : NetworkState = NetworkState(Status.SUCCESS, "Success")
        val LOADING : NetworkState = NetworkState(Status.RUNNING, "Running")
        val ERROR : NetworkState = NetworkState(Status.FAILED, "Something went Wrong !")

    }

}