package poly.nhatro.ui;

import poly.nhatro.entity.DienNuoc;

public interface dienNuocController extends CrudController<DienNuoc>{
    void init();
    @Override
    void open();
    @Override
    void setForm(DienNuoc entity);
    @Override
    DienNuoc getForm();
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