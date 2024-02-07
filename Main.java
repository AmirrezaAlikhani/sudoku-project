import java.util.Scanner;



class Table
{
    Cell[][][][] cells;
    int number_of_cages;
    Cage[] cages;
    int number_of_unvalued_cells;
    List_of_cells[] list_of_unvalued_cells;
   
    Table() {
        int i, j, k, l;
        this.cells= new Cell[3][3][3][3];
        for( i=0 ; i<3 ; i++ )
            for( j=0 ; j<3 ; j++ )
                for( k=0 ; k<3 ; k++ )
                    for( l=0 ; l<3 ; l++ )
                        this.cells[i][j][k][l]= new Cell( this, i, j, k, l);
        this.number_of_cages=0;
        this.number_of_unvalued_cells=81;
        this.list_of_unvalued_cells= new List_of_cells[9];
        for( i=0 ; i<9 ; i++ )
            this.list_of_unvalued_cells[i]= new List_of_cells();
        for( i=0 ; i<3 ; i++ )
            for( j=0 ; j<3 ; j++ )
                for( k=0 ; k<3 ; k++ )
                    for( l=0 ; l<3 ; l++ )
                        this.list_of_unvalued_cells[8].add_cell(cells[i][j][k][l]); }
       
    void update_domains_of_cells() {
        int i, j, k, l;
        boolean is_changed=true;
        while(is_changed){
            is_changed=false;
            for( i=0 ; i<3 ; i++ )
                for( j=0 ; j<3 ; j++ )
                    for( k=0 ; k<3 ; k++ )
                        for( l=0 ; l<3 ; l++ )
                            is_changed= (is_changed||this.cells[i][j][k][l].update_domain_in_cage());} }
                       
    Cell take_cell_with_least_domain(){
        int i;
        for( i=0 ; i<9 ; i++ )
            if( this.list_of_unvalued_cells[i].first_cell!=null )
                return this.list_of_unvalued_cells[i].first_cell.this_cell;
        return null; }
       
    Table table_copy() {
        int i, j, k, l, m;
        Linking_cell current_cell = new Linking_cell();
        Table copy_table = new Table();
        copy_table.number_of_cages=this.number_of_cages;
        copy_table.cages= new Cage[copy_table.number_of_cages];
        for( i=0 ; i<copy_table.number_of_cages ; i++ ){
            copy_table.cages[i]= new Cage( copy_table, i);
            current_cell=this.cages[i].cells.first_cell;
            for( j=0 ; j<this.cages[i].cells.number_of_cells ; j++ ){
                copy_table.cages[i].cells.add_cell(copy_table.cells[current_cell.this_cell.x_of_its_box][current_cell.this_cell.y_of_its_box][current_cell.this_cell.x_in_its_box][current_cell.this_cell.y_in_its_box]);
                current_cell=current_cell.next_cell;}
            copy_table.cages[i].sum_of_values_of_cells=this.cages[i].sum_of_values_of_cells;
            copy_table.cages[i].sum_of_values_of_unvalued_cells=this.cages[i].sum_of_values_of_unvalued_cells;}
        for( i=0 ; i<3 ; i++ )
            for( j=0 ; j<3 ; j++ )
                for( k=0 ; k<3 ; k++ )
                    for( l=0 ; l<3 ; l++ ){
                        copy_table.cells[i][j][k][l].its_cage=copy_table.cages[this.cells[i][j][k][l].its_cage.number_in_its_table];
                        copy_table.cells[i][j][k][l].value=this.cells[i][j][k][l].value;
                        for( m=0 ; m<9 ; m++ )
                            copy_table.cells[i][j][k][l].set_of_allowed_values[m]=this.cells[i][j][k][l].set_of_allowed_values[m];
                        copy_table.cells[i][j][k][l].number_of_allowed_values=this.cells[i][j][k][l].number_of_allowed_values;
                        copy_table.cells[i][j][k][l].minimum_allowed_value=this.cells[i][j][k][l].minimum_allowed_value;
                        copy_table.cells[i][j][k][l].maximum_allowed_value=this.cells[i][j][k][l].maximum_allowed_value;
                        copy_table.cells[i][j][k][l].number_of_unvalued_neighbors=this.cells[i][j][k][l].number_of_unvalued_neighbors;}
        copy_table.number_of_unvalued_cells=this.number_of_unvalued_cells;
        copy_table.list_of_unvalued_cells[8].first_cell=null;
        copy_table.list_of_unvalued_cells[8].number_of_cells=0;
        for( i=0 ; i<9 ; i++ ){
            current_cell=this.list_of_unvalued_cells[i].first_cell;
            for( j=0 ; j<this.list_of_unvalued_cells[i].number_of_cells ; j++ ){
                copy_table.list_of_unvalued_cells[i].add_cell(copy_table.cells[current_cell.this_cell.x_of_its_box][current_cell.this_cell.y_of_its_box][current_cell.this_cell.x_in_its_box][current_cell.this_cell.y_in_its_box]);
                current_cell=current_cell.next_cell;}}
        return copy_table; }
       
        Table solve() {
            while( this.list_of_unvalued_cells[0].number_of_cells!=0 ){
                this.list_of_unvalued_cells[0].first_cell.this_cell.valuation(this.list_of_unvalued_cells[0].first_cell.this_cell.minimum_allowed_value);
                this.update_domains_of_cells();}
            if( this.take_cell_with_least_domain()==null )
                return this;
            else{
                int min, max, i, j;
                Table new_table = new Table();
                new_table=this.table_copy();
                min=new_table.take_cell_with_least_domain().minimum_allowed_value;
                max=new_table.take_cell_with_least_domain().maximum_allowed_value;
                for( i=min-1 ; i<max ; i++ ){
                    new_table=this.table_copy();
                    if( new_table.take_cell_with_least_domain().set_of_allowed_values[i]==true ){
                        new_table.take_cell_with_least_domain().valuation(i+1);
                        new_table.update_domains_of_cells();
                        new_table=new_table.solve();
                        if( new_table.number_of_unvalued_cells==0 )
                            return new_table;}}
            return this;} }
}

class Cell
{
    Table its_table;
    int x_of_its_box;
    int y_of_its_box;
    int x_in_its_box;
    int y_in_its_box;
    Cage its_cage;
    int value;
    boolean[] set_of_allowed_values;
    int number_of_allowed_values;
    int minimum_allowed_value;
    int maximum_allowed_value;
    int number_of_unvalued_neighbors;
   
    Cell(Table the_table,int i,int j,int k,int l) {
        this.its_table=the_table;
        this.x_of_its_box=i;
        this.y_of_its_box=j;
        this.x_in_its_box=k;
        this.y_in_its_box=l;
        this.value=0;
        this.set_of_allowed_values= new boolean[9];
        int m;
        for( m=0 ; m<9 ; m++ )
            this.set_of_allowed_values[m]=true;
        this.number_of_allowed_values=9;
        this.minimum_allowed_value=1;
        this.maximum_allowed_value=9;
        this.number_of_unvalued_neighbors=20; }
       
    void number_of_allowed_values_decrement() {
        this.its_table.list_of_unvalued_cells[this.number_of_allowed_values-1].delete_cell(this);
        this.number_of_allowed_values--;
        if( 0<this.number_of_allowed_values )
            this.its_table.list_of_unvalued_cells[this.number_of_allowed_values-1].add_cell(this); }
       
    boolean update_domain_in_cage() {
        boolean is_changed=false;
        if( this.value==0 ){
            int sum_of_minimums=0, sum_of_maximums=0, i;
            Linking_cell current_cell = new Linking_cell();
            current_cell=this.its_cage.cells.first_cell;
            for( i=0 ; i<this.its_cage.cells.number_of_cells ; i++ ){
                if( !(current_cell.this_cell==this) )
                    if( current_cell.this_cell.value==0 ){
                        sum_of_minimums=sum_of_minimums+current_cell.this_cell.minimum_allowed_value;
                        sum_of_maximums=sum_of_maximums+current_cell.this_cell.maximum_allowed_value;}
                current_cell=current_cell.next_cell;}
            if( (this.its_cage.sum_of_values_of_unvalued_cells-sum_of_minimums)<9 )
                for( i=8 ; ((this.its_cage.sum_of_values_of_unvalued_cells-sum_of_minimums)<i+1)&&((-1)<i) ; i-- )
                    if( this.set_of_allowed_values[i]==true ){
                        this.set_of_allowed_values[i]=false;
                        this.number_of_allowed_values_decrement();
                        is_changed=true;}
            if( 1<(this.its_cage.sum_of_values_of_unvalued_cells-sum_of_maximums) )
                for( i=0 ; (i+1<(this.its_cage.sum_of_values_of_unvalued_cells-sum_of_maximums))&&(i<9) ; i++ )
                    if( this.set_of_allowed_values[i]==true ){
                        this.set_of_allowed_values[i]=false;
                        this.number_of_allowed_values_decrement();
                        is_changed=true;}
            this.minimum_allowed_value=0;
            for( i=0 ; i<9 ; i++ )
                if( this.set_of_allowed_values[i]==true ){
                    this.minimum_allowed_value=i+1;
                    break;}
            this.maximum_allowed_value=0;
            for( i=8 ; (-1)<i ; i-- )
                if( this.set_of_allowed_values[i]==true ){
                    this.maximum_allowed_value= i+1;
                    break;}} 
        return is_changed; }
       
    void remove_from_domain(int number) {
        if( this.set_of_allowed_values[number-1]==true ){
            this.set_of_allowed_values[number-1]=false;
            this.number_of_allowed_values_decrement();
            int i;
            this.minimum_allowed_value=0;
            for( i=0 ; i<9 ; i++ )
                if( this.set_of_allowed_values[i]==true ){
                    this.minimum_allowed_value=i+1;
                    break;}
            this.maximum_allowed_value=0;
            for( i=8 ; -1<i ; i-- )
                if( this.set_of_allowed_values[i]==true ){
                    this.maximum_allowed_value=i+1;
                    break;}
            Linking_cell current_cell = new Linking_cell();
            current_cell=this.its_cage.cells.first_cell;
            for( i=0 ; i<this.its_cage.cells.number_of_cells ; i++ ){
                if( current_cell.this_cell!=this )
                    current_cell.this_cell.update_domain_in_cage();
                current_cell=current_cell.next_cell;}} }
       
    void update_relateds(int number) {
        int i, j, k, l;
        Linking_cell current_cell = new Linking_cell();
        this.its_cage.sum_of_values_of_unvalued_cells-=this.value;
        this.its_table.number_of_unvalued_cells--;
        for( j=0 ; j<3 ; j++ )
            for( l=0 ; l<3 ; l++ )
                if( this.its_table.cells[this.x_of_its_box][j][this.x_in_its_box][l]!=this ){
                    this.its_table.cells[this.x_of_its_box][j][this.x_in_its_box][l].remove_from_domain(number);
                    this.its_table.cells[this.x_of_its_box][j][this.x_in_its_box][l].number_of_unvalued_neighbors--;}
        for( i=0 ; i<3 ; i++ )
            for( k=0 ; k<3 ; k++ )
                if( this.its_table.cells[i][this.y_of_its_box][k][this.y_in_its_box]!=this ){
                    this.its_table.cells[i][this.y_of_its_box][k][this.y_in_its_box].remove_from_domain(number);
                    this.its_table.cells[i][this.y_of_its_box][k][this.y_in_its_box].number_of_unvalued_neighbors--;}
        for( k=0 ; k<3 ; k++ )
            for( l=0 ; l<3 ; l++ )
                if( this.its_table.cells[this.x_of_its_box][this.y_of_its_box][k][l]!=this ){
                    this.its_table.cells[this.x_of_its_box][this.y_of_its_box][k][l].remove_from_domain(number);
                    this.its_table.cells[this.x_of_its_box][this.y_of_its_box][k][l].number_of_unvalued_neighbors--;}
        current_cell=this.its_cage.cells.first_cell;
        for( i=0 ; i<this.its_cage.cells.number_of_cells ; i++ ){
            if( current_cell.this_cell!=this ){
                current_cell.this_cell.number_of_unvalued_neighbors--;
                current_cell.this_cell.update_domain_in_cage();}
            current_cell=current_cell.next_cell;} }
       
    boolean valuation(int number) {
        if( (number==0) && (this.value==0) )
            return true;
        else if( this.set_of_allowed_values[number-1]==true ){
            this.value=number;
            int i;
            for( i=0 ; i<9 ; i++ )
                if( i!=(number-1) )
                    this.set_of_allowed_values[i]=false;
            this.its_table.list_of_unvalued_cells[this.number_of_allowed_values-1].delete_cell(this);
            this.number_of_allowed_values=1;
            this.minimum_allowed_value=number;
            this.maximum_allowed_value=number;
            this.update_relateds(number);
            return true;}
        return false; }
}

class Cage
{
    Table its_table;
    int number_in_its_table;
    List_of_cells cells;
    int sum_of_values_of_cells;
    int sum_of_values_of_unvalued_cells;
   
    Cage(Table the_table,int number) {
        this.its_table=the_table;
        this.number_in_its_table=number;
        this.cells= new List_of_cells();
        this.sum_of_values_of_cells=0;
        this.sum_of_values_of_unvalued_cells=0; }
}

class Linking_cell
{
    Cell this_cell;
    Linking_cell next_cell;
}

class List_of_cells
{
    Linking_cell first_cell;
    int number_of_cells;
   
    List_of_cells() {
        this.number_of_cells=0; }
       
    void add_cell(Cell the_cell) {
        Linking_cell new_cell = new Linking_cell();
        new_cell.this_cell=the_cell;
        if( this.number_of_cells==0 )
            this.first_cell=new_cell;
        else{
            new_cell.next_cell=this.first_cell;
            this.first_cell=new_cell;}
        this.number_of_cells++; }
       
    void delete_cell(Cell the_cell) {
        if( this.first_cell.this_cell==the_cell )
            this.first_cell=this.first_cell.next_cell;
        else{
            Linking_cell current_cell = new Linking_cell();
            current_cell=this.first_cell;
            while( current_cell.next_cell.this_cell!=the_cell )
                current_cell=current_cell.next_cell;
            current_cell.next_cell=current_cell.next_cell.next_cell;}
        this.number_of_cells--; }
}

class Value_and_its_attributes
{
    int value;
    int number_of_not_assigned_values;
    int number_of_domains_with_this;
    int the_difference;
    
    Value_and_its_attributes(int v) {
        this.value=v;
        this.number_of_not_assigned_values=9;
        this.number_of_domains_with_this=81;
        this.the_difference=72; }
}



public class Main
{
   
    public static void main(String[] args)
    {
        int i, j, k, l, m;
        String auxiliary_parse;
        boolean is_successful;
        Linking_cell auxiliary_linking_cell = new Linking_cell();
        Scanner input = new Scanner(System.in);
        Table the_original_table = new Table();
        int[] the_values_of_the_initial_table = new int[81];
       
        for( i=0 ; i<81 ; i++ )
            the_values_of_the_initial_table[i]=input.nextInt();
        the_original_table.number_of_cages=input.nextInt();
        if( the_original_table.number_of_cages==0 ){
            the_original_table.number_of_cages=1;
            the_original_table.cages= new Cage[1];
            the_original_table.cages[0]= new Cage( the_original_table, 0);
            for( i=0 ; i<3 ; i++ )
                for( j=0 ; j<3 ; j++ )
                    for( k=0 ; k<3 ; k++ )
                        for( l=0 ; l<3 ; l++ ){
                            the_original_table.cages[0].cells.add_cell(the_original_table.cells[i][j][k][l]);
                            the_original_table.cells[i][j][k][l].its_cage=the_original_table.cages[0];
                            the_original_table.cells[i][j][k][l].number_of_unvalued_neighbors=100;}
            the_original_table.cages[0].sum_of_values_of_cells=405;
            the_original_table.cages[0].sum_of_values_of_unvalued_cells=405;}
        else{
            the_original_table.cages= new Cage[the_original_table.number_of_cages];
            for( i=0 ; i<the_original_table.number_of_cages ; i++ )
                the_original_table.cages[i]= new Cage( the_original_table, i);
            for( m=0 ; m<the_original_table.number_of_cages ; m++ ){
                auxiliary_parse=input.next();
                while(!auxiliary_parse.equals(">")){
                    i= ((Integer.parseInt(auxiliary_parse)%10)-1)/3;
                    j= ((Integer.parseInt(auxiliary_parse)/10)-1)/3;
                    k= ((Integer.parseInt(auxiliary_parse)%10)-1)%3;
                    l= ((Integer.parseInt(auxiliary_parse)/10)-1)%3;
                    the_original_table.cages[m].cells.add_cell(the_original_table.cells[i][j][k][l]);
                    the_original_table.cells[i][j][k][l].its_cage=the_original_table.cages[m];
                    auxiliary_parse=input.next();}
                auxiliary_linking_cell=the_original_table.cages[m].cells.first_cell;
                for( i=0 ; i<the_original_table.cages[m].cells.number_of_cells ; i++ ){
                    auxiliary_linking_cell.this_cell.number_of_unvalued_neighbors+=(the_original_table.cages[m].cells.number_of_cells-1);
                    auxiliary_linking_cell=auxiliary_linking_cell.next_cell;}
                the_original_table.cages[m].sum_of_values_of_cells=input.nextInt();
                the_original_table.cages[m].sum_of_values_of_unvalued_cells=the_original_table.cages[m].sum_of_values_of_cells;}}
       
        the_original_table.update_domains_of_cells();
        m=0;    
        for( j=0 ; j<3 ; j++ )
            for( l=0 ; l<3 ; l++ )
                for( i=0 ; i<3 ; i++ )
                    for( k=0 ; k<3 ; k++ ){
                        is_successful=the_original_table.cells[i][j][k][l].valuation(the_values_of_the_initial_table[m]);
                        if(!is_successful){
                            System.out.println("NO");
                            System.exit(0);}
                        m++;}
                        
        the_original_table=the_original_table.solve();
       
        System.out.println();
        for( i=0 ; i<3 ; i++ )
            for( j=0 ; j<3 ; j++ )
                for( k=0 ; k<3 ; k++ )
                    for( l=0 ; l<3 ; l++ ){
                        if( the_original_table.cells[i][j][k][l].value==0 ){
                            System.out.println("NO");
                            System.exit(0);}}
        for( j=0 ; j<2 ; j++ ){
            for( l=0 ; l<3 ; l++ ){
                for( i=0 ; i<2 ; i++ ){
                    for( k=0 ; k<2 ; k++ )
                        System.out.print(the_original_table.cells[i][j][k][l].value+" ");
                    System.out.print(the_original_table.cells[i][j][k][l].value+"|");}
                for( k=0 ; k<3 ; k++ )
                    System.out.print(the_original_table.cells[i][j][k][l].value+" ");
                System.out.println();}
            for( l=0 ; l<17 ; l++ )
                System.out.print("-");
            System.out.println();}
        for( l=0 ; l<3 ; l++ ){
            for( i=0 ; i<2 ; i++ ){
                for( k=0 ; k<2 ; k++ )
                    System.out.print(the_original_table.cells[i][j][k][l].value+" ");
                System.out.print(the_original_table.cells[i][j][k][l].value+"|");}
            for( k=0 ; k<3 ; k++ )
                System.out.print(the_original_table.cells[i][j][k][l].value+" ");
            System.out.println();}
    }

}