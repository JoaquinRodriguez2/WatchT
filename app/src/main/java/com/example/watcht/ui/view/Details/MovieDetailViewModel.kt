package com.example.watcht.ui.view.Details


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.watcht.data.model.movieDetails.MovieDetails
import com.example.watcht.domain.DeleteMovieFromDataBaseUseCase
import com.example.watcht.domain.GetMoviesDetailsUseCase
import com.example.watcht.domain.SaveMovieToDataBaseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject



sealed class DetailState {


    object LoadingMovieDetails : DetailState()
    object ErrorMovieDetails : DetailState()
    data class SuccessMovieDetails(val response: MovieDetails) : DetailState()


}


@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val detailuseCase: GetMoviesDetailsUseCase,
    private val saveMovieUseCase: SaveMovieToDataBaseUseCase,
    private val deleteMovieUseCase:DeleteMovieFromDataBaseUseCase
) :
    ViewModel() {
    private val _movieDetailState = MutableLiveData<DetailState>()
    val movieDetailState: LiveData<DetailState> = _movieDetailState

    fun getDetailsUseCase(id: Int) {
        _movieDetailState.value = DetailState.LoadingMovieDetails

        detailuseCase.getDetails(id).observeForever { stateCall ->
            when (stateCall) {
                is GetMoviesDetailsUseCase.StateCall.SuccessCall -> {
                    _movieDetailState.value =
                        DetailState.SuccessMovieDetails(stateCall.movieDetails)
                }
                is GetMoviesDetailsUseCase.StateCall.LoadingCall -> {
                    _movieDetailState.value =
                        DetailState.LoadingMovieDetails
                }
                is GetMoviesDetailsUseCase.StateCall.ErrorCall -> {
                    _movieDetailState.value =
                        DetailState.ErrorMovieDetails
                }
            }
        }
    }

    fun saveMovie(movie:MovieDetails){
        viewModelScope.launch {
            saveMovieUseCase.saveMovie(movie)
        }
    }



    fun deleteMovie(movie:MovieDetails){
        viewModelScope.launch {
            deleteMovieUseCase.deleteMovie(movie)
        }
    }
}