package tunanh.test_app.api

sealed class APiResponse<T> constructor(val loadingStatus: LoadingStatus) {
    class DataLoading<T> : APiResponse<T>(LoadingStatus.Loading)
    class DataIdle<T> : APiResponse<T>(LoadingStatus.Idle)
    class DataError<T> : APiResponse<T>(LoadingStatus.Error)
    data class DataSuccess<T>(val body: T) : APiResponse<T>(LoadingStatus.Success)
}

enum class LoadingStatus {
    Idle, Loading, Success, Error
}