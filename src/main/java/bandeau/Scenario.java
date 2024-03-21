package bandeau;
import java.util.List;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Classe utilitaire pour représenter la classe-association UML
 */
class ScenarioElement {

    Effect effect;
    int repeats;

    ScenarioElement(Effect e, int r) {
        effect = e;
        repeats = r;
    }
}
/**
 * Un scenario mémorise une liste d'effets, et le nombre de repetitions pour chaque effet
 * Un scenario sait se jouer sur un bandeau.
 */
public class Scenario {

    private final List<ScenarioElement> myElements = new LinkedList<>();
    private boolean isPlaying = false;
    /**
     * Ajouter un effect au scenario.
     *
     * @param e l'effet à ajouter
     * @param repeats le nombre de répétitions pour cet effet
     */
    public synchronized void addEffect(Effect e, int repeats) {
        if (!isPlaying){
            myElements.add(new ScenarioElement(e, repeats));
        } else {
            System.out.println("Impossible d'ajouter un effet pendant la lecture du scénario");;
        }
    }

    /**
     * Jouer ce scenario sur un bandeau
     *
     * @param b le bandeau ou s'afficher.
     */
    public void playOn(Bandeau b) {
        if(!isPlaying){
            isPlaying = true;
            ExecutorService executor = Executors.newFixedThreadPool(3); // Créer un pool de threads avec 3 threads

            for (int i = 0; i < 3; i++) {
                Bandeau bandeau = new Bandeau();
                executor.execute(() -> executeScenario(bandeau));
            }

            executor.shutdown(); // Arrêter le pool de threads une fois que les tâches sont terminées
        } else {
            System.out.println("Impossible de jouer un scénario sur un bandeau déjà en cours de lecture.");
        }
    }
    private void executeScenario(Bandeau b) {
        for (ScenarioElement element : myElements) {
            for (int repeats = 0; repeats < element.repeats; repeats++) {
                element.effect.playOn(b);
            }
        }
        isPlaying = false; // Marquer le scénario comme terminé
    }
}
