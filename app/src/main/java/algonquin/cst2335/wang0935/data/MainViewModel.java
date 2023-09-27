package algonquin.cst2335.wang0935.data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {
        public MutableLiveData<String> userString = new MutableLiveData("");

        public MutableLiveData<Boolean> CoffeeOrNot = new MutableLiveData<>(false);

        public MutableLiveData<Boolean> getCoffeeOrNot() {
                return CoffeeOrNot;
        }
        public void setCoffeeOrNot(boolean value) {
                CoffeeOrNot.setValue(value);
        }


}
