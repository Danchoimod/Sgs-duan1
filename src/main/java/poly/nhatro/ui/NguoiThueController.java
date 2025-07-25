package poly.nhatro.ui;

import poly.nhatro.entity.NguoiThue;

/**
 *
 * @author tranthithuyngan
 */
interface NguoiThueController extends CrudController<NguoiThue> {
    
 void init();
    @Override
    void open();
    @Override
    void setForm(NguoiThue entity);
    @Override
    NguoiThue getForm();
    @Override
    void create();
    @Override
    void update();
    @Override
    void delete();
    @Override
    void clear();
    @Override
    void edit();
    @Override
    void setEditable(boolean editable);
    @Override
    void fillToTable();
}
