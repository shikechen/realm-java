package com.tightdb;

import java.nio.ByteBuffer;
import java.util.Date;


/**
 * This class represents a view of a particular table. We can think of
 * a tableview is a subset of a table. It contains less than or
 * equal to the number of entries of a table. A table view is often a
 * result of a query.
 *
 * The view don't copy data from the table, but merely has a list of
 * row-references into the original table with the real data.
 *
 * The class serves as a base class of all table view. It is a raw level table view, users
 * are not encouraged to use this class. Instead users are advised to use
 * the generated subclass version of the table.
 *
 * Let us assume we are going to keep track of a table to store the employees
 * of a company or an organization.
 *
 * Following is a small example how to use the autogenerated class of the
 * tableview. For that purpose we will first define the spec of an employee
 * entity
 *
 *  @DefineTable
 *  public class employee {
 *      String name;
 *      long age;
 *      boolean hired;
 *      byte[] imageData;
 *  }
 *
 * Once this class is compiled along with TightDB annotation processor
 * this will produce following classes.
 *
 * 1. Employee
 * 2. EmployeeTable
 * 3. EmployeeView.
 *
 * In this class context our interest is EmployeeView which will be inherited from
 * this class.
 *
 * The generated class will have more specialized method to do operations on TableView.
 *
 */
public class TableView implements TableOrView {
    protected boolean DEBUG = false; //true;

    /**
     * Creates a TableViewBase with a Java Object Table and a already created
     * native reference to a TableView. This method is not supposed to be
     * called by a user of this db. It is for internal use only.
     *
     * @param table The table.
     * @param nativePtr pointer to table.
     */
    protected TableView(long nativePtr, boolean immutable){
        this.immutable = immutable;
        this.tableView = null;
        this.nativePtr = nativePtr;
    }

    /**
     * Creates a TableView with already created Java TableView Object and a
     * native native TableView object reference. The method is not supposed to
     * be called by the user of the db. The method is for internal use only.
     *
     * @param tableView A table view.
     * @param nativePtr pointer to table.
     */
    protected TableView(TableView tableView, long nativePtr, boolean immutable){
        this.immutable = immutable;
        this.tableView = tableView;
        this.nativePtr = nativePtr;
    }

    /**
     * Checks whether this table is empty or not.
     *
     * @return true if empty, otherwise false.
     */
    public boolean isEmpty(){
        return size() == 0;
    }

    /**
     * Get the number of entries/rows of this table.
     *
     * @return The number of rows.
     */
    public long size() {
        return nativeSize(nativePtr);
    }

    protected native long nativeSize(long nativeViewPtr);

    /**
     * Get the value of the particular (integer) cell.
     *
     * @param columnIndex 0 based index value of the column.
     * @param rowIndex 0 based row value of the column.
     * @return value of the particular cell.
     */
    public long getLong(long columnIndex, long rowIndex){
        return nativeGetLong(nativePtr, columnIndex, rowIndex);
    }

    protected native long nativeGetLong(long nativeViewPtr, long columnIndex, long rowIndex);

    /**
     * Get the value of the particular (boolean) cell.
     *
     * @param columnIndex 0 based index value of the cell column.
     * @param rowIndex 0 based index of the row.
     * @return value of the particular cell.
     */
    public boolean getBoolean(long columnIndex, long rowIndex){
        return nativeGetBoolean(nativePtr, columnIndex, rowIndex);
    }

    protected native boolean nativeGetBoolean(long nativeViewPtr, long columnIndex, long rowIndex);

    /**
     * Get the value of the particular (float) cell.
     *
     * @param columnIndex 0 based index value of the cell column.
     * @param rowIndex 0 based index of the row.
     * @return value of the particular cell.
     */
    public float getFloat(long columnIndex, long rowIndex){
        return nativeGetFloat(nativePtr, columnIndex, rowIndex);
    }

    protected native float nativeGetFloat(long nativeViewPtr, long columnIndex, long rowIndex);

    /**
     * Get the value of the particular (double) cell.
     *
     * @param columnIndex 0 based index value of the cell column.
     * @param rowIndex 0 based index of the row.
     * @return value of the particular cell.
     */
    public double getDouble(long columnIndex, long rowIndex){
        return nativeGetDouble(nativePtr, columnIndex, rowIndex);
    }

    protected native double nativeGetDouble(long nativeViewPtr, long columnIndex, long rowIndex);

    /**
     * Get the value of the particular (date) cell.
     *
     * @param columnIndex 0 based index value of the cell column.
     * @param rowIndex 0 based index of the row.
     * @return value of the particular cell.
     */
    public Date getDate(long columnIndex, long rowIndex){
        return new Date(nativeGetDateTimeValue(nativePtr, columnIndex, rowIndex)*1000);
    }

    protected native long nativeGetDateTimeValue(long nativeViewPtr, long columnIndex, long rowIndex);

    /**
     * Get the value of a (string )cell.
     *
     * @param columnIndex 0 based index value of the column
     * @param rowIndex 0 based index of the row.
     * @return value of the particular cell
     */
    public String getString(long columnIndex, long rowIndex){
        return nativeGetString(nativePtr, columnIndex, rowIndex);
    }

    protected native String nativeGetString(long nativeViewPtr, long columnInde, long rowIndex);


    /**
     * Get the  value of a (binary) cell.
     *
     * @param columnIndex 0 based index value of the cell column
     * @param rowIndex 0 based index value of the cell row
     * @return value of the particular cell.
     */
    public ByteBuffer getBinaryByteBuffer(long columnIndex, long rowIndex){
        return nativeGetBinary(nativePtr, columnIndex, rowIndex);
    }

    protected native ByteBuffer nativeGetBinary(long nativeViewPtr, long columnIndex, long rowIndex);

    public byte[] getBinaryByteArray(long columnIndex, long rowIndex){
        return nativeGetByteArray(nativePtr, columnIndex, rowIndex);
    }

    protected native byte[] nativeGetByteArray(long nativePtr, long columnIndex, long rowIndex);

    //TODO: NEW!!!
    public ColumnType getMixedType(long columnIndex, long rowIndex) {
        return ColumnType.fromNativeValue(nativeGetMixedType(nativePtr, columnIndex, rowIndex));
    }

    protected native int nativeGetMixedType(long nativeViewPtr, long columnIndex, long rowIndex);

    public Mixed getMixed(long columnIndex, long rowIndex){
        return nativeGetMixed(nativePtr, columnIndex, rowIndex);
    }

    protected native Mixed nativeGetMixed(long nativeViewPtr, long columnIndex, long rowIndex);

    public Table getSubTable(long columnIndex, long rowIndex){
        return new Table(this, nativeGetSubTable(nativePtr, columnIndex, rowIndex), immutable);
    }

    protected native long nativeGetSubTable(long nativeViewPtr, long columnIndex, long rowIndex);

    public long getSubTableSize(long columnIndex, long rowIndex) {
        return nativeGetSubTableSize(nativePtr, columnIndex, rowIndex);
    }

    protected native long nativeGetSubTableSize(long nativeTablePtr, long columnIndex, long rowIndex);

    public void clearSubTable(long columnIndex, long rowIndex) {
        if (immutable) throwImmutable();
        nativeClearSubTable(nativePtr, columnIndex, rowIndex);
    }

    protected native void nativeClearSubTable(long nativeTablePtr, long columnIndex, long rowIndex);


    // Methods for setting values.

    /**
     * Sets the value for a particular (integer) cell.
     *
     * @param columnIndex column index of the cell
     * @param rowIndex row index of the cell
     * @param value
     */
    public void setLong(long columnIndex, long rowIndex, long value){
        if (immutable) throwImmutable();
        nativeSetLong(nativePtr, columnIndex, rowIndex, value);
    }

    protected native void nativeSetLong(long nativeViewPtr, long columnIndex, long rowIndex, long value);

    /**
     * Sets the value for a particular (boolean) cell.
     *
     * @param columnIndex column index of the cell
     * @param rowIndex row index of the cell
     * @param value
     */
    public void setBoolean(long columnIndex, long rowIndex, boolean value){
        if (immutable) throwImmutable();
        nativeSetBoolean(nativePtr, columnIndex, rowIndex, value);
    }

    protected native void nativeSetBoolean(long nativeViewPtr, long columnIndex, long rowIndex, boolean value);

    /**
     * Sets the value for a particular (float) cell.
     *
     * @param columnIndex column index of the cell
     * @param rowIndex row index of the cell
     * @param value
     */
    public void setFloat(long columnIndex, long rowIndex, float value){
        if (immutable) throwImmutable();
        nativeSetFloat(nativePtr, columnIndex, rowIndex, value);
    }

    protected native void nativeSetFloat(long nativeViewPtr, long columnIndex, long rowIndex, float value);

    /**
     * Sets the value for a particular (double) cell.
     *
     * @param columnIndex column index of the cell
     * @param rowIndex row index of the cell
     * @param value
     */
    public void setDouble(long columnIndex, long rowIndex, double value){
        if (immutable) throwImmutable();
        nativeSetDouble(nativePtr, columnIndex, rowIndex, value);
    }

    protected native void nativeSetDouble(long nativeViewPtr, long columnIndex, long rowIndex, double value);

    /**
     * Sets the value for a particular (date) cell.
     *
     * @param columnIndex column index of the cell
     * @param rowIndex row index of the cell
     * @param value
     */
    public void setDate(long columnIndex, long rowIndex, Date value){
        if (immutable) throwImmutable();
        nativeSetDateTimeValue(nativePtr, columnIndex, rowIndex, value.getTime()/1000);
    }

    protected native void nativeSetDateTimeValue(long nativePtr, long columnIndex, long rowIndex, long dateTimeValue);

    /**
     * Sets the value for a particular (sting) cell.
     *
     * @param columnIndex column index of the cell
     * @param rowIndex row index of the cell
     * @param value
     */
    public void setString(long columnIndex, long rowIndex, String value){
        if (immutable) throwImmutable();
        nativeSetString(nativePtr, columnIndex, rowIndex, value);
    }

    protected native void nativeSetString(long nativeViewPtr, long columnIndex, long rowIndex, String value);

    /**
     * Sets the value for a particular (binary) cell.
     *
     * @param columnIndex column index of the cell
     * @param rowIndex row index of the cell
     * @param data
     */
    public void setBinaryByteBuffer(long columnIndex, long rowIndex, ByteBuffer data){
        if (immutable) throwImmutable();
        nativeSetBinary(nativePtr, columnIndex, rowIndex, data);
    }

    protected native void nativeSetBinary(long nativeViewPtr, long columnIndex, long rowIndex, ByteBuffer data);

    public void setBinaryByteArray(long columnIndex, long rowIndex, byte[] data){
        if (immutable) throwImmutable();
        nativeSetByteArray(nativePtr, columnIndex, rowIndex, data);
    }

    protected native void nativeSetByteArray(long nativePtr, long columnIndex, long rowIndex, byte[] data);

    /**
     * Sets the value for a particular (mixed typed) cell.
     *
     * @param columnIndex column index of the cell
     * @param rowIndex row index of the cell
     * @param data
     */
    public void setMixed(long columnIndex, long rowIndex, Mixed data){
        if (immutable) throwImmutable();
        nativeSetMixed(nativePtr, columnIndex, rowIndex, data);
    }

    protected native void nativeSetMixed(long nativeViewPtr, long columnIndex, long rowIndex, Mixed value);

    /**
     * Add the value for to all cells in the column.
     *
     * @param columnIndex column index of the cell
     * @param value
     */
    //!!!TODO: New
    public void addLong(long columnIndex, long value) {
        if (immutable) throwImmutable();
        nativeAddInt(nativePtr, columnIndex, value);
    }

    protected native void nativeAddInt(long nativeViewPtr, long columnIndex, long value);

    // Methods for deleting.
    public void clear(){
        if (immutable) throwImmutable();
        nativeClear(nativePtr);
    }

    protected native void nativeClear(long nativeViewPtr);

    /**
     * Removes a particular row identified by the index from the tableview.
     * The corresponding row of the underlying table also get deleted.
     *
     * @param rowIndex the row index
     */
    public void remove(long rowIndex){
        if (immutable) throwImmutable();
        nativeRemoveRow(nativePtr, rowIndex);
    }

    protected native void nativeRemoveRow(long nativeViewPtr, long rowIndex);

    public void removeLast() {
        if (immutable) throwImmutable();
        if (!isEmpty()) {
            nativeRemoveRow(nativePtr, size() - 1);
        }
    }

    // Search for first match

    public long findFirstLong(long columnIndex, long value){
        return nativeFindFirstInt(nativePtr, columnIndex, value);
    }

    protected native long nativeFindFirstInt(long nativeTableViewPtr, long columnIndex, long value);

    //!!!TODO: New
    public long findFirstBoolean(long columnIndex, boolean value) {
        return nativeFindFirstBool(nativePtr, columnIndex, value);
    }

    protected native long nativeFindFirstBool(long nativePtr, long columnIndex, boolean value);

    //!!!TODO: New
    public long findFirstFloat(long columnIndex, float value) {
        return nativeFindFirstFloat(nativePtr, columnIndex, value);
    }

    protected native long nativeFindFirstFloat(long nativePtr, long columnIndex, float value);

    //!!!TODO: New
    public long findFirstDouble(long columnIndex, double value) {
        return nativeFindFirstDouble(nativePtr, columnIndex, value);
    }

    protected native long nativeFindFirstDouble(long nativePtr, long columnIndex, double value);

    //!!!TODO: New
    public long findFirstDate(long columnIndex, Date date) {
        return nativeFindFirstDate(nativePtr, columnIndex, date.getTime()/1000);
    }

    protected native long nativeFindFirstDate(long nativeTablePtr, long columnIndex, long dateTimeValue);

    public long findFirstString(long columnIndex, String value){
        return nativeFindFirstString(nativePtr, columnIndex, value);
    }

    protected native long nativeFindFirstString(long nativePtr, long columnIndex, String value);


    // Search for all matches

    // TODO..
    public long lowerBoundLong(long columnIndex, long value) {
        throw new RuntimeException("Not implemented yet");
    }
    public long upperBoundLong(long columnIndex, long value) {
        throw new RuntimeException("Not implemented yet");
    }


    public TableView findAllLong(long columnIndex, long value){
        return new TableView(this,  nativeFindAllInt(nativePtr, columnIndex, value), immutable);
    }

    protected native long nativeFindAllInt(long nativePtr, long columnIndex, long value);

    //!!!TODO: New
    public TableView findAllBoolean(long columnIndex, boolean value) {
        return new TableView(this, nativeFindAllBool(nativePtr, columnIndex, value), immutable);
    }

    protected native long nativeFindAllBool(long nativePtr, long columnIndex, boolean value);

    //!!!TODO: New
    public TableView findAllFloat(long columnIndex, float value) {
        return new TableView(this, nativeFindAllFloat(nativePtr, columnIndex, value), immutable);
    }

    protected native long nativeFindAllFloat(long nativePtr, long columnIndex, float value);

    //!!!TODO: New
    public TableView findAllDouble(long columnIndex, double value) {
        return new TableView(this, nativeFindAllDouble(nativePtr, columnIndex, value), immutable);
    }

    protected native long nativeFindAllDouble(long nativePtr, long columnIndex, double value);

    //!!!TODO: New
    public TableView findAllDate(long columnIndex, Date date) {
        return new TableView(this, nativeFindAllDate(nativePtr, columnIndex, date.getTime()/1000), immutable);
    }

    protected native long nativeFindAllDate(long nativePtr, long columnIndex, long dateTimeValue);

    public TableView findAllString(long columnIndex, String value){
        return new TableView(this, nativeFindAllString(nativePtr, columnIndex, value), immutable);
    }

    protected native long nativeFindAllString(long nativePtr, long columnIndex, String value);



    //
    // Integer Aggregates
    //

    /**
     * Calculate the sum of the values in a particular column of this
     * tableview.
     *
     * Note: the type of the column marked by the columnIndex has to be of
     * type ColumnType.ColumnTypeInt.
     *
     * @param columnIndex column index
     * @return the sum of the values in the column
     */
    public long sum(long columnIndex){
        return nativeSum(nativePtr, columnIndex);
    }

    protected native long nativeSum(long nativeViewPtr, long columnIndex);

    /**
     * Returns the maximum value of the cells in a column.
     *
     * Note: for this method to work the Type of the column
     * identified by the columnIndex has to be ColumnType.ColumnTypeInt.
     *
     * @param columnIndex column index
     * @return the maximum value
     */
    public long maximum(long columnIndex){
        return nativeMaximum(nativePtr, columnIndex);
    }

    protected native long nativeMaximum(long nativeViewPtr, long columnIndex);

    /**
     * Returns the minimum value of the cells in a column.
     *
     * Note: for this method to work the Type of the column
     * identified by the columnIndex has to be ColumnType.ColumnTypeInt.
     *
     * @param columnIndex column index
     * @return the minimum value
     */
    public long minimum(long columnIndex){
        return nativeMinimum(nativePtr, columnIndex);
    }

    protected native long nativeMinimum(long nativeViewPtr, long columnIndex);

    public double average(long columnIndex) {
        return nativeAverage(nativePtr, columnIndex);
    }

    protected native double nativeAverage(long nativePtr, long columnIndex);


    // Float aggregates

    public double sumFloat(long columnIndex){
        return nativeSumFloat(nativePtr, columnIndex);
    }
    protected native double nativeSumFloat(long nativeViewPtr, long columnIndex);

    public float maximumFloat(long columnIndex){
        return nativeMaximumFloat(nativePtr, columnIndex);
    }
    protected native float nativeMaximumFloat(long nativeViewPtr, long columnIndex);

    public float minimumFloat(long columnIndex){
        return nativeMinimumFloat(nativePtr, columnIndex);
    }

    protected native float nativeMinimumFloat(long nativeViewPtr, long columnIndex);

    public double averageFloat(long columnIndex) {
        return nativeAverageFloat(nativePtr, columnIndex);
    }

    protected native double nativeAverageFloat(long nativePtr, long columnIndex);


    // Double aggregates

    public double sumDouble(long columnIndex){
        return nativeSumDouble(nativePtr, columnIndex);
    }
    protected native double nativeSumDouble(long nativeViewPtr, long columnIndex);

    public double maximumDouble(long columnIndex){
        return nativeMaximumDouble(nativePtr, columnIndex);
    }
    protected native double nativeMaximumDouble(long nativeViewPtr, long columnIndex);

    public double minimumDouble(long columnIndex){
        return nativeMinimumDouble(nativePtr, columnIndex);
    }

    protected native double nativeMinimumDouble(long nativeViewPtr, long columnIndex);

    public double averageDouble(long columnIndex) {
        return nativeAverageDouble(nativePtr, columnIndex);
    }

    protected native double nativeAverageDouble(long nativePtr, long columnIndex);


    // Sorting

    public enum Order { ascending, descending };

    public void sort(long columnIndex, Order order) {
        if (immutable) throwImmutable();
        nativeSort(nativePtr, columnIndex, (order == Order.ascending));
    }

    public void sort(long columnIndex) {
        if (immutable) throwImmutable();
        nativeSort(nativePtr, columnIndex, true);
    }

    protected native void nativeSort(long nativeTableViewPtr, long columnIndex, boolean ascending);


    protected native long createNativeTableView(Table table, long nativeTablePtr);

    public void finalize(){
        close();
    }

    private synchronized void close(){
        if (DEBUG) System.err.println("==== TableView CLOSE, ptr= " + nativePtr);    	
        if (nativePtr == 0)
            return;
        nativeClose(nativePtr);
        nativePtr = 0;
    }
    
    public void private_debug_close(){
    	close();
    }

    protected native void nativeClose(long nativeViewPtr);

    public String toJson() {
        return nativeToJson(nativePtr);
    }

    protected native String nativeToJson(long nativeViewPtr);

    public String toString() {
        return nativeToString(nativePtr, 500);
    }
    
    public String toString(long maxRows) {
        return nativeToString(nativePtr, maxRows);
    }

    protected native String nativeToString(long nativeTablePtr, long maxRows);

    public String rowToString(long rowIndex) {
        return nativeRowToString(nativePtr, rowIndex);
    }

    protected native String nativeRowToString(long nativeTablePtr, long rowIndex);

    private void throwImmutable()
    {
        throw new IllegalStateException("Mutable method call during read transaction.");
    }

    protected long nativePtr;
    protected boolean immutable = false;
    protected TableView tableView;

    @Override
    public long lookup(String value) {
        // TODO: implement
        throw new RuntimeException("Not implemented yet.");
    }

    @Override
    public long count(long columnIndex, String value) {
        // TODO: implement
        throw new RuntimeException("Not implemented yet.");
    }
}
