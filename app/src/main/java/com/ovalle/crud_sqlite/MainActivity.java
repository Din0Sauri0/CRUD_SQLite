package com.ovalle.crud_sqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.ovalle.crud_sqlite.baseDatos.Utilidades;
import com.ovalle.crud_sqlite.baseDatos.conexionSQLiteHelper;
import com.ovalle.crud_sqlite.baseDatos.entidades.Cliente;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    //widget
    private EditText txtId, txtNombre, txtUsername, txtPassword;
    private Button btnCrear, btnActualizar, btnEliminar;
    private ListView ListViewClientes;
    //manipulacion de BD
    conexionSQLiteHelper conn = new conexionSQLiteHelper(MainActivity.this, null, null, 1);
    ArrayList<Cliente> arrayClientes;
    Cliente cliente;
    ArrayAdapter<Cliente> adaptadorListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //UI Referencias
        txtId = findViewById(R.id.txtId);
        txtNombre = findViewById(R.id.txtNombre);
        txtUsername = findViewById(R.id.txtUsername);
        txtPassword = findViewById(R.id.txtPassword);
        btnCrear = findViewById(R.id.btnCrear);
        btnActualizar = findViewById(R.id.btnActualizar);
        btnEliminar = findViewById(R.id.btnEliminar);
        ListViewClientes = findViewById(R.id.ListViewClientes);
        // Started Object
        arrayClientes = new ArrayList<>();
        adaptadorListView = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, arrayClientes);
        obtenerClientes();

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //capturar datos escritos por el usuario
                int id = Integer.parseInt(txtId.getText().toString());
                //enviar datos al metodo insertarCliente()
                eliminarCliente(id);
                limpiarCajas();
            }
        });

        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //capturar datos escritos por el usuario
                int id = Integer.parseInt(txtId.getText().toString());
                String nombre = txtNombre.getText().toString();
                String userName = txtUsername.getText().toString();
                String password = txtPassword.getText().toString();
                //enviar datos al metodo insertarCliente()
                actualizarCliente(id, nombre, userName, password);
                limpiarCajas();
            }
        });

        btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //capturar datos escritos por el usuario
                int id = Integer.parseInt(txtId.getText().toString());
                String nombre = txtNombre.getText().toString();
                String userName = txtUsername.getText().toString();
                String password = txtPassword.getText().toString();
                //enviar datos al metodo insertarCliente()
                insertarCliente(id, nombre, userName, password);
                limpiarCajas();
            }
        });

    }

    private void eliminarCliente(int id) {
        SQLiteDatabase database = conn.getWritableDatabase();
        String[] parametros = { String.valueOf(id) };
        database.delete(Utilidades.NOMBRE_TABLE, Utilidades.CAMPO_ID+"=?", parametros);
        Toast.makeText(MainActivity.this, "Cliente eliminado", Toast.LENGTH_LONG).show();
        database.close();
        obtenerClientes();
    }

    private void actualizarCliente(int id, String nombre, String userName, String password) {
        SQLiteDatabase database = conn.getWritableDatabase();
        String[] parametros = { String.valueOf(id) };
        ContentValues values = new ContentValues();
        values.put(Utilidades.CAMPO_NOMBRE, nombre);
        values.put(Utilidades.CAMPO_USERNAME, userName);
        values.put(Utilidades.CAMPO_PASSWORD, password);

        database.update(Utilidades.NOMBRE_TABLE,values,Utilidades.CAMPO_ID+"=?", parametros);
        Toast.makeText(MainActivity.this, "Cliente actualizado", Toast.LENGTH_LONG).show();
        database.close();
        obtenerClientes();
    }

    public void insertarCliente(int id, String nombre, String userName, String password){
        SQLiteDatabase database = conn.getWritableDatabase();
        //Crear un Contenedro de valores e insertar
        ContentValues values = new ContentValues();
        values.put(Utilidades.CAMPO_ID, id);
        values.put(Utilidades.CAMPO_NOMBRE, nombre);
        values.put(Utilidades.CAMPO_USERNAME, userName);
        values.put(Utilidades.CAMPO_PASSWORD, password);

        database.insert(Utilidades.NOMBRE_TABLE, Utilidades.CAMPO_ID, values);
        Toast.makeText(MainActivity.this, "Cliente guardado", Toast.LENGTH_LONG).show();
        database.close();
        obtenerClientes();
    }

    public void obtenerClientes(){
        adaptadorListView.clear();
        SQLiteDatabase database = conn.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM "+Utilidades.NOMBRE_TABLE,null);
        while(cursor.moveToNext()){
            cliente = new Cliente(
                    cursor.getInt(0),//id
                    cursor.getString(1),//nombre
                    cursor.getString(2),//username
                    cursor.getString(3)//password
            );
            arrayClientes.add(cliente);
        }
        adaptadorListView = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, arrayClientes);
        ListViewClientes.setAdapter(adaptadorListView);
        cursor.close();
        conn.close();
    }

    public Cliente selectOne(int id){
        SQLiteDatabase database = conn.getReadableDatabase();
        String[] parametros = {String.valueOf(id)};
        String[] campos = {Utilidades.CAMPO_ID, Utilidades.CAMPO_NOMBRE, Utilidades.CAMPO_USERNAME};
        Cursor cursor = database.query(Utilidades.NOMBRE_TABLE,campos,Utilidades.CAMPO_ID+"=?",parametros,null,null,null);
        Cliente usuario = null;
        while (cursor.moveToNext()){
            cliente = new Cliente(cursor.getInt(0),cursor.getString(1),cursor.getString(2), cursor.getString(3));
        }
        return cliente;
    }

    public void limpiarCajas(){
        txtId.setText("");
        txtNombre.setText("");
        txtUsername.setText("");
        txtPassword.setText("");
    }
}