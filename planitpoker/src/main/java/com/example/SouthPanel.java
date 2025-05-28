package four;
import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;
import javax.swing.*;
import three.StoriesNanny;
import three.Story;

/**
 * Stories organized in tabs.
 * The first tab contains the active stories, and the second one contains the completed stories.
 *
 * @author javiergs
 */
public class SouthPanel extends JPanel {
	
	public SouthPanel() {
		setBackground(new Color(161, 190, 239));
		setLayout(new BorderLayout());
		JTabbedPane storyTabs = new JTabbedPane();
		
		//Create StoriesNanny instance to access stories
		StoriesNanny storiesNanny = new StoriesNanny(null); // Pass appropriate Main instance
		storiesNanny.importStories(); // Import stories to populate the lists
		Queue<Story> newStories = storiesNanny.getNewStories(); // Access newStories
	
		LinkedList<Story> prevStories = storiesNanny.getPrevStories();

		
		//Populate active stories with newStories
		StringBuilder activeStoriesText = new StringBuilder();
		for (Story story : newStories) {
			activeStoriesText.append(story.getTitle()).append("\n");
		}
		JTextArea activeStories = new JTextArea(activeStoriesText.toString());
		
		 //Populate completed stories with prevStories
		StringBuilder completedStoriesText = new StringBuilder();
		for (Story story : prevStories) {
			completedStoriesText.append(String.format("%-50s %10s%n", story.getTitle(), story.getScore())).append("\n");
		}
		JTextArea completedStories = new JTextArea(completedStoriesText.toString());
		
		//Adjust table height
		JScrollPane activeScrollPane = new JScrollPane(activeStories);
		activeScrollPane.setPreferredSize(new Dimension(400, 150)); //Adjust height
		
		JScrollPane completedScrollPane = new JScrollPane(completedStories);
		completedScrollPane.setPreferredSize(new Dimension(400, 150)); //Adjust height
		
		storyTabs.addTab("Active Stories", activeScrollPane);
		storyTabs.addTab("Completed Stories", completedScrollPane);
		
		add(storyTabs, BorderLayout.CENTER);
	}

}


