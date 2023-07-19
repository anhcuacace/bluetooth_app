package tunanh.test_app.pay

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import tunanh.test_app.pre.ConnectIdTech

class PayViewModel : ViewModel() {

    private val _cardDataState = MutableStateFlow(PayModel("", "", ""))
    val cardDataState: StateFlow<PayModel> = _cardDataState

    fun listener(context: Context) {
        connect.listenerIdTech(context)
    }

    private val connect = ConnectIdTech.getInstance()

    init {
        connect.setSwipeListener(_cardDataState)
        viewModelScope.launch {

        }
    }
}