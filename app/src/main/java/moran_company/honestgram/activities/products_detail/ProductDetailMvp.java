package moran_company.honestgram.activities.products_detail;

import moran_company.honestgram.base_mvp.BaseMvp;
import moran_company.honestgram.data.Chats;
import moran_company.honestgram.data.Goods;

/**
 * Created by roman on 30.01.2018.
 */

public interface ProductDetailMvp {

    interface View extends BaseMvp.View{
        void showChat(Chats chat);
    }

    interface Presenter extends BaseMvp.Presenter{

        void checkChat(Goods goods,String message);

    }
}
