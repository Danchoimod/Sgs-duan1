package poly.nhatro.ui;

import poly.nhatro.entity.HoaDon;

/**
 *
 * @author tranthithuyngan
 */
public interface HoaDonController extends CrudController<HoaDon> {
    void init();
    @Override
    void open();
    @Override
    void setForm(HoaDon entity);
    @Override
    HoaDon getForm();
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
