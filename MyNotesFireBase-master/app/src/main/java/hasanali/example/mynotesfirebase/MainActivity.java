package hasanali.example.mynotesfirebase;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements NoteFragment.OnNoteListInteractionListener{
    boolean displayingEditor = false;
    ArrayList<Note> notes = new ArrayList<>();
    Note editingNote;
    private static final String TAG = "Firebase Demo";
    ListenerRegistration listenerRegistration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!displayingEditor){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.container,NoteFragment.newInstance(notes),"list_note");
            ft.commit();
        }else {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container,EditNoteFragment.newInstance(editingNote.getContent()));
            ft.addToBackStack(null);
            ft.commit();
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("notes").orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    Log.e("Firebase","error retrieving notes", error);
                    return;
                }
                notes.clear();
                for(QueryDocumentSnapshot doc : value){
                    notes.add(doc.toObject(Note.class));
                }
                NoteFragment listFragment = (NoteFragment) getSupportFragmentManager().findFragmentByTag("list_note");
                listFragment.updateNotes(notes);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("onOptionsItemSelected", item.getTitle().toString());
        displayingEditor = !displayingEditor;
        invalidateOptionsMenu();
        switch (item.getItemId()) {
            case R.id.action_new:
                editingNote = createNote();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.container,EditNoteFragment.newInstance(""),"edit_note");
                ft.addToBackStack(null);
                ft.commit();
                return true;
            case R.id.action_close:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private Note createNote() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Note note = new Note();
        note.setId(db.collection("notes").document().getId());
        return note;
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        Log.d("onPrepareOptionsMenu new visible", menu.findItem(R.id.action_new).isVisible() + "");
        menu.findItem(R.id.action_new).setVisible(!displayingEditor);
        menu.findItem(R.id.action_close).setVisible(displayingEditor);
        return super.onPrepareOptionsMenu(menu);

    }

    @Override
    public void onNoteSelected(Note note) {
        editingNote = note;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container,EditNoteFragment.newInstance(editingNote.getContent()),"edit _note");
        ft.addToBackStack(null);
        ft.commit();
        displayingEditor = !displayingEditor;
        invalidateOptionsMenu();

    }

    @Override
    public void onBackPressed() {
        EditNoteFragment editFragment = (EditNoteFragment)
                getSupportFragmentManager().findFragmentByTag("edit_note");
        String content = null;
        if (editFragment != null){
            content = editFragment.getContent();
        }
        super.onBackPressed();
        if (content !=null) {
            saveContent(editingNote, content);
        }
    }


    private void saveContent(Note note, String content) {
        if (note.getContent() == null || !note.getContent().equals(content)) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            note.setDate(new Timestamp(new Date()));
            note.setContent(content);
            db.collection("notes").document(note.getId()).set(note);
        }else{
            Log.d(TAG, "notes: " + notes);
            NoteFragment listFragment = (NoteFragment)
                    getSupportFragmentManager().findFragmentByTag("list_note");
            listFragment.updateNotes(notes);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        listenerRegistration.remove();
    }
}