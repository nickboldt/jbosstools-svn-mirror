/*
 * DO NOT EDIT.  THIS FILE IS GENERATED FROM
 * /builds/slave/mozilla-1.9.2-linux-xulrunner/build/editor/idl/nsITableEditor.idl
 */

package org.mozilla.interfaces;

public interface nsITableEditor extends nsISupports {

  String NS_ITABLEEDITOR_IID =
    "{4805e684-49b9-11d3-9ce4-ed60bd6cb5bc}";

  short eNoSearch = 0;

  short ePreviousColumn = 1;

  short ePreviousRow = 2;

  /** Insert table methods
    * Insert relative to the selected cell or the 
    *  cell enclosing the selection anchor
    * The selection is collapsed and is left in the new cell
    *  at the same row,col location as the original anchor cell
    *
    * @param aNumber    Number of items to insert
    * @param aAfter     If TRUE, insert after the current cell,
    *                     else insert before current cell
    */
  void insertTableCell(int aNumber, boolean aAfter);

  void insertTableColumn(int aNumber, boolean aAfter);

  void insertTableRow(int aNumber, boolean aAfter);

  /** Delete table methods
    * Delete starting at the selected cell or the 
    *  cell (or table) enclosing the selection anchor
    * The selection is collapsed and is left in the 
    *  cell at the same row,col location as
    *  the previous selection anchor, if possible,
    *  else in the closest neigboring cell
    *
    * @param aNumber    Number of items to insert/delete
    */
  void deleteTable();

  /** Delete just the cell contents
    * This is what should happen when Delete key is used
    *   for selected cells, to minimize upsetting the table layout
    */
  void deleteTableCellContents();

  /** Delete cell elements as well as contents
    * @param aNumber   Number of contiguous cells, rows, or columns
    *
    * When there are more than 1 selected cells, aNumber is ignored.
    * For Delete Rows or Columns, the complete columns or rows are 
    *  determined by the selected cells. E.g., to delete 2 complete rows,
    *  user simply selects a cell in each, and they don't
    *  have to be contiguous.
    */
  void deleteTableCell(int aNumber);

  void deleteTableColumn(int aNumber);

  void deleteTableRow(int aNumber);

  /** Table Selection methods
    * Selecting a row or column actually
    * selects all cells (not TR in the case of rows)
    */
  void selectTableCell();

  /** Select a rectangular block of cells:
    *  all cells falling within the row/column index of aStartCell
    *  to through the row/column index of the aEndCell
    *  aStartCell can be any location relative to aEndCell,
    *   as long as they are in the same table
    *  @param aStartCell  starting cell in block
    *  @param aEndCell    ending cell in block
    */
  void selectBlockOfCells(nsIDOMElement aStartCell, nsIDOMElement aEndCell);

  void selectTableRow();

  void selectTableColumn();

  void selectTable();

  void selectAllTableCells();

  /** Create a new TD or TH element, the opposite type of the supplied aSourceCell
    *   1. Copy all attributes from aSourceCell to the new cell
    *   2. Move all contents of aSourceCell to the new cell
    *   3. Replace aSourceCell in the table with the new cell
    *
    *  @param aSourceCell   The cell to be replaced
    *  @return              The new cell that replaces aSourceCell
    */
  nsIDOMElement switchTableCellHeaderType(nsIDOMElement aSourceCell);

  /** Merges contents of all selected cells
    * for selected cells that are adjacent,
    * this will result in a larger cell with appropriate 
    * rowspan and colspan, and original cells are deleted
    * The resulting cell is in the location of the 
    *   cell at the upper-left corner of the adjacent
    *   block of selected cells
    *
    * @param aMergeNonContiguousContents:  
    *       If true: 
    *         Non-contiguous cells are not deleted,
    *         but their contents are still moved 
    *         to the upper-left cell
    *       If false: contiguous cells are ignored
    *
    * If there are no selected cells,
    *   and selection or caret is in a cell,
    *   that cell and the one to the right 
    *   are merged
    */
  void joinTableCells(boolean aMergeNonContiguousContents);

  /** Split a cell that has rowspan and/or colspan > 0
    *   into cells such that all new cells have 
    *   rowspan = 1 and colspan = 1
    *  All of the contents are not touched --
    *   they will appear to be in the upper-left cell 
    */
  void splitTableCell();

  /** Scan through all rows and add cells as needed so 
    *   all locations in the cellmap are occupied.
    *   Used after inserting single cells or pasting
    *   a collection of cells that extend past the
    *   previous size of the table
    * If aTable is null, it uses table enclosing the selection anchor
    * This doesn't doesn't change the selection,
    *   thus it can be used to fixup all tables
    *   in a page independant of the selection
    */
  void normalizeTable(nsIDOMElement aTable);

  /** Get the row an column index from the layout's cellmap
    * If aCell is null, it will try to find enclosing table of selection anchor
    * 
    */
  void getCellIndexes(nsIDOMElement aCell, int[] aRowIndex, int[] aColIndex);

  /** Get the number of rows and columns in a table from the layout's cellmap
    * If aTable is null, it will try to find enclosing table of selection ancho
    * Note that all rows in table will not have this many because of 
    * ROWSPAN effects or if table is not "rectangular" (has short rows)
    */
  void getTableSize(nsIDOMElement aTable, int[] aRowCount, int[] aColCount);

  /** Get a cell element at cellmap grid coordinates
    * A cell that spans across multiple cellmap locations will
    *   be returned multiple times, once for each location it occupies
    *
    * @param aTable                   A table in the document
    * @param aRowIndex, aColIndex     The 0-based cellmap indexes
    *
    * (in C++ returns: NS_EDITOR_ELEMENT_NOT_FOUND if an element is not found
    *  passes NS_SUCCEEDED macro)
    *
    *   You can scan for all cells in a row or column
    *   by iterating through the appropriate indexes
    *   until the returned aCell is null
    */
  nsIDOMElement getCellAt(nsIDOMElement aTable, int aRowIndex, int aColIndex);

  /** Get a cell at cellmap grid coordinates and associated data
    * A cell that spans across multiple cellmap locations will
    *   be returned multiple times, once for each location it occupies
    * Examine the returned aStartRowIndex and aStartColIndex to see 
    *   if it is in the same layout column or layout row:
    *   A "layout row" is all cells sharing the same top edge
    *   A "layout column" is all cells sharing the same left edge
    *   This is important to determine what to do when inserting or deleting a column or row
    * 
    *  @param aTable                   A table in the document
    *  @param aRowIndex, aColIndex     The 0-based cellmap indexes
    * returns values:
    *  @param aCell                    The cell at this cellmap location
    *  @param aStartRowIndex           The row index where cell starts
    *  @param aStartColIndex           The col index where cell starts
    *  @param aRowSpan                 May be 0 (to span down entire table) or number of cells spanned
    *  @param aColSpan                 May be 0 (to span across entire table) or number of cells spanned
    *  @param aActualRowSpan           The actual number of cellmap locations (rows) spanned by the cell
    *  @param aActualColSpan           The actual number of cellmap locations (columns) spanned by the cell
    *  @param aIsSelected
    *  @param 
    *
    * (in C++ returns: NS_EDITOR_ELEMENT_NOT_FOUND if an element is not found
    *  passes NS_SUCCEEDED macro)
    */
  void getCellDataAt(nsIDOMElement aTable, int aRowIndex, int aColIndex, nsIDOMElement[] aCell, int[] aStartRowIndex, int[] aStartColIndex, int[] aRowSpan, int[] aColSpan, int[] aActualRowSpan, int[] aActualColSpan, boolean[] aIsSelected);

  /** Get the first row element in a table
    *
    * @return            The row at the requested index
    *                    Returns null if there are no rows in table
    * (in C++ returns: NS_EDITOR_ELEMENT_NOT_FOUND if an element is not found
    *  passes NS_SUCCEEDED macro)
    */
  nsIDOMNode getFirstRow(nsIDOMElement aTableElement);

  /** Get the next row element starting the search from aTableElement
    *
    * @param aTableElement Any TR or child-of-TR element in the document
    *
    * @return            The row to start search from
    *                    and the row returned from the search
    *                    Returns null if there isn't another row
    * (in C++ returns: NS_EDITOR_ELEMENT_NOT_FOUND if an element is not found
    *  passes NS_SUCCEEDED macro)
    */
  nsIDOMNode getNextRow(nsIDOMNode aTableElement);

  /** Preferred direction to search for neighboring cell
    * when trying to locate a cell to place caret in after
    * a table editing action. 
    * Used for aDirection param in SetSelectionAfterTableEdit
    */
/** Reset a selected cell or collapsed selection (the caret) after table editing
    *
    * @param aTable      A table in the document
    * @param aRow        The row ...
    * @param aCol        ... and column defining the cell
    *                    where we will try to place the caret
    * @param aSelected   If true, we select the whole cell instead of setting caret
    * @param aDirection  If cell at (aCol, aRow) is not found,
    *                    search for previous cell in the same
    *                    column (aPreviousColumn) or row (ePreviousRow)
    *                    or don't search for another cell (aNoSearch)
    *                    If no cell is found, caret is place just before table;
    *                    and if that fails, at beginning of document.
    *                    Thus we generally don't worry about the return value
    *                     and can use the nsSetSelectionAfterTableEdit stack-based 
    *                     object to insure we reset the caret in a table-editing method.
    */
  void setSelectionAfterTableEdit(nsIDOMElement aTable, int aRow, int aCol, int aDirection, boolean aSelected);

  /** Examine the current selection and find
    *   a selected TABLE, TD or TH, or TR element.
    *   or return the parent TD or TH if selection is inside a table cell
    *   Returns null if no table element is found.
    *
    * @param aTagName         The tagname of returned element
    *                         Note that "td" will be returned if name
    *                         is actually "th"
    * @param aCount           How many table elements were selected
    *                         This tells us if we have multiple cells selected
    *                           (0 if element is a parent cell of selection)
    * @return                 The table element (table, row, or first selected cell)
    *
    */
  nsIDOMElement getSelectedOrParentTableElement(String[] aTagName, int[] aCount);

  /** Generally used after GetSelectedOrParentTableElement
    *   to test if selected cells are complete rows or columns
    * 
    * @param aElement           Any table or cell element or any element
    *                           inside a table
    *                           Used to get enclosing table. 
    *                           If null, selection's anchorNode is used
    * 
    * @return
    *     0                        aCellElement was not a cell
    *                              (returned result = NS_ERROR_FAILURE)
    *     TABLESELECTION_CELL      There are 1 or more cells selected but
    *                              complete rows or columns are not selected
    *     TABLESELECTION_ROW       All cells are in 1 or more rows
    *                              and in each row, all cells selected
    *                              Note: This is the value if all rows
    *                              (thus all cells) are selected
    *     TABLESELECTION_COLUMN    All cells are in 1 or more columns
    *                              and in each column, all cells are selected
    */
  long getSelectedCellsType(nsIDOMElement aElement);

  /** Get first selected element from first selection range.
    *   (If multiple cells were selected this is the first in the order they were selected)
    * Assumes cell-selection model where each cell
    * is in a separate range (selection parent node is table row)
    * @param aCell     [OUT] Selected cell or null if ranges don't contain
    *                  cell selections
    * @param aRange    [OUT] Optional: if not null, return the selection range 
    *                     associated with the cell
    * Returns the DOM cell element
    *   (in C++: returns NS_EDITOR_ELEMENT_NOT_FOUND if an element is not found
    *    passes NS_SUCCEEDED macro)
    */
  nsIDOMElement getFirstSelectedCell(nsIDOMRange[] aRange);

  /** Get first selected element in the table
    *   This is the upper-left-most selected cell in table,
    *   ignoring the order that the user selected them (order in the selection ranges)
    * Assumes cell-selection model where each cell
    * is in a separate range (selection parent node is table row)
    * @param aCell       Selected cell or null if ranges don't contain
    *                    cell selections
    * @param aRowIndex   Optional: if not null, return row index of 1st cell
    * @param aColIndex   Optional: if not null, return column index of 1st cell
    *
    * Returns the DOM cell element
    *   (in C++: returns NS_EDITOR_ELEMENT_NOT_FOUND if an element is not found
    *    passes NS_SUCCEEDED macro)
    */
  nsIDOMElement getFirstSelectedCellInTable(int[] aRowIndex, int[] aColIndex);

  /** Get next selected cell element from first selection range.
    * Assumes cell-selection model where each cell
    * is in a separate range (selection parent node is table row)
    * Always call GetFirstSelectedCell() to initialize stored index of "next" cell
    * @param aCell     Selected cell or null if no more selected cells
    *                     or ranges don't contain cell selections
    * @param aRange    Optional: if not null, return the selection range 
    *                     associated with the cell
    *
    * Returns the DOM cell element
    *   (in C++: returns NS_EDITOR_ELEMENT_NOT_FOUND if an element is not found
    *    passes NS_SUCCEEDED macro)
    */
  nsIDOMElement getNextSelectedCell(nsIDOMRange[] aRange);

}