package com.example.skinwise.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.skinwise.R
import com.example.skinwise.model.Lesson
import com.example.skinwise.Manager.LessonProgressManager
import com.google.android.material.appbar.MaterialToolbar
import com.example.skinwise.utils.AccentUtils


class EducationActivity : AppCompatActivity() {

    private lateinit var educationRecyclerView: RecyclerView

    val lessons = listOf(
        Lesson(
            "Pașii unei rutine corecte",
            "Învață ordinea corectă de aplicare a produselor pentru o piele sănătoasă și strălucitoare.",
            """
        O rutină corectă te ajută să maximizezi eficiența produselor. Iată pașii recomandați:
        
        1️⃣ **Curățare** – Elimină impuritățile și excesul de sebum cu un cleanser blând.
        
        2️⃣ **Tonifiere** – Restaurează pH-ul pielii și pregătește-o pentru produsele următoare.
        
        3️⃣ **Seruri** – Concentrează-te pe nevoile pielii (ex: hidratare, antiacneic, anti-aging).
        
        4️⃣ **Cremă hidratantă** – Sigilează ingredientele active și menține hidratarea.
        
        5️⃣ **Protecție solară (AM)** – Cel mai important pas pentru a preveni îmbătrânirea prematură și petele pigmentare.

        📝 *Sfat:* Aplică produsele de la cele mai fluide (ser) la cele mai groase (cremă).
    """.trimIndent(),
            R.drawable.routine
        ),
        Lesson(
            "Ce tip de piele ai?",
            "Identificarea corectă a tipului de piele este cheia pentru a alege produsele potrivite.",
            """
        🧐 Tipurile principale de piele:
        
        • **Piele normală** – Textură echilibrată, fără exces de sebum sau uscăciune.
        
        • **Piele uscată** – Se simte aspră sau strânsă după curățare. Poate avea descuamări.
        
        • **Piele grasă** – Luciu vizibil în zona T, pori dilatați, predispusă la acnee.
        
        • **Piele mixtă** – Gras în zona T (frunte, nas, bărbie), uscată pe obraji.
        
        🔍 *Test simplu:* Curăță fața și așteaptă 1h fără produse. Observă cum se simte pielea:
        - Dacă strânge ➝ uscată
        - Dacă lucește ➝ grasă
        - Dacă e parțial ➝ mixtă
        
        🎯 Alege produse adaptate fiecărui tip de piele pentru cele mai bune rezultate!
    """.trimIndent(),
            R.drawable.skin_types
        ),
        Lesson(
            "Importanța protecției solare",
            "Află de ce este esențială protecția solară zilnică și cum alegi produsul potrivit.",
            """
        🌞 De ce este importantă protecția solară?

        • Protejează pielea împotriva razelor UVA și UVB, care pot cauza arsuri, îmbătrânire prematură și cancer de piele.
        • Previne apariția petelor pigmentare și a ridurilor fine.
        • Reduce riscul de reacții inflamatorii și sensibilizare a pielii.

        🧴 Tipuri de protecție solară:

        • **Minerală (fizică)** – conține zinc oxide sau titanium dioxide. Formează o barieră pe piele.
        • **Chimică** – absoarbe radiațiile UV. Textură ușoară, ideală pentru utilizarea zilnică.

        ✅ Sfaturi utile:

        • Aplică cu 15-30 de minute înainte de expunere.
        • Reaplică la fiecare 2 ore sau după înot/transpirație.
        • Folosește minim SPF 30 zilnic, chiar și pe timp înnorat.

        🧡 Bonus: Protecția solară nu este doar pentru plajă! Este o parte esențială a rutinei zilnice, indiferent de sezon.
    """.trimIndent(),
            R.drawable.sun_protection // imaginea trebuie să o adaugi în res/drawable
        ),
        Lesson(
            "Totul despre hidratarea pielii",
            "Descoperă de ce hidratarea este esențială și cum să o adaptezi tipului tău de piele.",
            """
        💧 De ce este importantă hidratarea?

        • Menține bariera naturală de protecție a pielii.
        • Previne senzația de uscăciune și iritații.
        • Reduce apariția liniilor fine și a semnelor de oboseală.

        🧴 Tipuri de ingrediente hidratante:

        • **Umectanți** – atrag apă în piele (ex: glicerina, acidul hialuronic).
        • **Emolienți** – netezesc pielea (ex: squalane, uleiuri naturale).
        • **Ocluzive** – creează un strat protector (ex: vaselină, dimethicone).

        ✅ Sfaturi:
        - Aplică hidratanții pe pielea umedă pentru eficiență maximă.
        - Alege texturi ușoare pentru pielea grasă și mai bogate pentru cea uscată.
        - Folosește hidratarea de 2 ori pe zi: dimineața și seara.

        🌟 O piele hidratată = o piele fericită și sănătoasă!
    """.trimIndent(),
            R.drawable.hydration // Asigură-te că ai adăugat imaginea în drawable cu numele hydration
        ),
        Lesson(
            "Exfolierea corectă a pielii",
            "Află cum și când să exfoliezi pielea pentru a menține un ten luminos și curat.",
            """
    🔄 De ce este importantă exfolierea?

    • Îndepărtează celulele moarte de la suprafața pielii.
    • Previne porii înfundați și apariția coșurilor.
    • Stimulează regenerarea celulară și uniformizează textura pielii.
    • Ajută celelalte produse să pătrundă mai eficient în piele.

    ✨ Tipuri de exfoliere:

    • **Mecanică** – conține particule abrazive (scruburi). Se masează ușor pe piele.
    • **Chimică** – folosește acizi (AHA, BHA, PHA) pentru a dizolva celulele moarte.
      - **AHA** (ex: acid glicolic, lactic): pentru pielea uscată sau ternă.
      - **BHA** (ex: acid salicilic): ideal pentru pielea grasă sau acneică.
      - **PHA**: mai blând, perfect pentru pielea sensibilă.

    ⚠️ Cum să o folosești corect:

    • Exfoliază de 1-2 ori pe săptămână, în funcție de tipul pielii.
    • Evită combinarea cu retinoizi sau alți acizi puternici în aceeași rutină.
    • Hidratează bine după exfoliere.
    • Nu uita de **SPF** – exfolierea crește sensibilitatea la soare!

    🌟 *Sfat:* Dacă simți usturime, iritație sau descuamare excesivă, redu frecvența sau alege un exfoliant mai blând.

    """.trimIndent(),
            R.drawable.exfoliation // Adaugă o imagine reprezentativă în drawable cu numele exfoliation
        ),
        Lesson(
            "Ingrediente populare și rolul lor",
            "Descoperă cele mai folosite ingrediente din skincare și cum te pot ajuta.",
            """
    🔬 Skincare-ul modern pune accent pe ingrediente active care oferă rezultate vizibile. Iată câteva dintre cele mai populare:

    • **Acid Hialuronic** – Hidratează intens și conferă volum pielii. Potrivit pentru toate tipurile de ten.

    • **Niacinamide** – Uniformizează tenul, reduce roșeața și diminuează porii. Ideal pentru pielea mixtă/grasă.

    • **Retinol (Vitamina A)** – Stimulează regenerarea celulară, reduce ridurile și combate acneea. Se folosește doar seara și necesită protecție solară.

    • **Vitamina C** – Iluminează tenul, estompează petele pigmentare și stimulează producția de colagen.

    • **AHA (ex: acid glicolic)** – Exfoliază ușor stratul superior al pielii. Excelent pentru ten uscat sau tern.

    • **BHA (acid salicilic)** – Curăță porii în profunzime. Eficient pentru ten acneic sau gras.

    ✅ *Sfat:* Introdu ingredientele treptat în rutină și nu combina prea multe active deodată. Pielea are nevoie de timp pentru adaptare.

    🌿 Bonus: Citește mereu eticheta produsului și evită combinațiile iritante, cum ar fi retinol + AHA/BHA.

    """.trimIndent(),
            R.drawable.ingredients // Asigură-te că ai o imagine pastelată, reprezentativă cu ingrediente (borcane, sticluțe, etc.)
        ),
        Lesson(
            title = "Îngrijirea tenului sensibil",
            description = "Sfaturi esențiale pentru un ten sensibil și reactiv.",
            fullText = """
        Tenul sensibil necesită o atenție specială deoarece reacționează ușor la factori externi precum temperaturile extreme, parfumuri sau ingrediente iritante.

        🔹 Curățare:
        Folosește un gel blând de curățare, fără parfum sau alcool.

        🔹 Hidratare:
        Optează pentru creme calmante cu ingrediente precum panthenol, aloe vera, centella asiatica sau madecassoside.

        🔹 Protecție solară:
        Protecția este esențială. Alege SPF-uri fizice (cu oxid de zinc sau dioxid de titan) fără parfum.

        🔹 Evită:
        Evită exfolianții agresivi, alcoolul denaturat, parfumurile și uleiurile esențiale.

        🔹 Extra:
        Introdu produsele noi treptat și testează-le pe o zonă mică înainte de aplicarea completă.

        Îngrijirea constantă și blândă este cheia unui ten sensibil echilibrat! 😊
    """.trimIndent(),
            imageResId = R.drawable.sensitive_skin // va trebui să adaugi imaginea în drawable cu acest nume
        ),
        Lesson(
            title = "Mituri în îngrijirea pielii",
            description = "Descoperă adevărul din spatele celor mai răspândite mituri.",
            fullText = """
        ❌ Mit 1: Pielea grasă nu are nevoie de hidratare.
        ✔️ Adevăr: Chiar și pielea grasă are nevoie de hidratare. Lipsa acesteia poate duce la o producție și mai mare de sebum.

        ❌ Mit 2: Dacă un produs ustură, înseamnă că funcționează.
        ✔️ Adevăr: Usturimea este adesea un semn de iritație. Produsele eficiente nu trebuie să provoace disconfort.

        ❌ Mit 3: Produsele naturale sunt întotdeauna mai bune.
        ✔️ Adevăr: Nu tot ce e natural e sigur pentru piele. De exemplu, uleiurile esențiale pot irita pielea sensibilă.

        ❌ Mit 4: Trebuie să schimbi constant produsele de îngrijire.
        ✔️ Adevăr: Dacă un produs funcționează pentru pielea ta, nu este nevoie să-l schimbi.
        
        ❌ Mit 5: Soarele ajută la vindecarea coșurilor.
        ✔️ Adevăr: Expunerea la soare poate agrava acneea pe termen lung și crește riscul de pete și îmbătrânire prematură.
        
        ❌ Mit 6: Cu cât exfoliezi mai des, cu atât mai bine.
        ✔️ Adevăr: Exfolierea excesivă poate distruge bariera pielii, provocând roșeață și iritații. 
        
        ❌ Mit 7: Tenul bronzat este un semn de piele sănătoasă.
        ✔️ Adevăr: Bronzul este o reacție la deteriorarea pielii provocată de razele UV.


        ✅ Sfaturi:
        - Citește etichetele cu atenție.
        - Nu te baza doar pe trenduri.
        - Ascultă nevoile propriei tale pieli.
    """.trimIndent(),
            imageResId = R.drawable.myths // <- asigură-te că ai această imagine adăugată în res/drawable
        )





        // Adaugă mai multe lecții aici
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_education)

        val toolbar = findViewById<MaterialToolbar>(R.id.topAppBarEducation)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = null // Elimină titlul default ("Skinwise")
        toolbar.title = "Lecții utile" // Titlul tău personalizat
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.beige_text))


        findViewById<MaterialToolbar>(R.id.topAppBarEducation).setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val recyclerView = findViewById<RecyclerView>(R.id.educationRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = EducationAdapter(lessons) { lesson ->
            val intent = Intent(this, EducationDetailActivity::class.java)
            intent.putExtra("lesson", lesson)
            startActivity(intent)
        }
    }
    override fun onResume() {
        super.onResume()

        val toolbar = findViewById<MaterialToolbar>(R.id.topAppBarEducation)
        AccentUtils.applyAccentFromPrefs(this, toolbar)

        }
    }

