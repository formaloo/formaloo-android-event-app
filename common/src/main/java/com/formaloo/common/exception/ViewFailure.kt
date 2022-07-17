package com.formaloo.common.exception


class ViewFailure {
    class responseError(msg: String?) : Failure.FeatureFailure(msg)
}
