package pl.edu.pw.elka.pszt;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.text.DecimalFormat;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.erichseifert.gral.data.DataSeries;
import de.erichseifert.gral.data.DataSource;
import de.erichseifert.gral.data.DataTable;
import de.erichseifert.gral.plots.Plot;
import de.erichseifert.gral.plots.XYPlot;
import de.erichseifert.gral.plots.areas.AreaRenderer;
import de.erichseifert.gral.plots.areas.DefaultAreaRenderer2D;
import de.erichseifert.gral.plots.axes.AxisRenderer;
import de.erichseifert.gral.plots.lines.DefaultLineRenderer2D;
import de.erichseifert.gral.plots.lines.LineRenderer;
import de.erichseifert.gral.plots.points.PointRenderer;
import de.erichseifert.gral.ui.InteractivePanel;
import de.erichseifert.gral.util.GraphicsUtils;

/*
 * Class that is used to show statistics after multiple generations
 * @author Marcin Kubik
 * @author Mikołaj Markiewicz
 * @author Krzysztof Opasiak
 */

@SuppressWarnings("serial")
public class Statistics extends JFrame {

	/** Vector of  best adaptations */
	private final Vector<Double> bestAdaptations;
	
	/** Vector of worst adaptations */
	private final Vector<Double> worstAdaptations;

	/** Population that is represented by the statistics */
	private final Population p;

	/** For some measurements */
	double time;

	/**
	 * c-tor Starts counting, how much time has passed
	 * 
	 * @param p
	 *            First population
	 */
	public Statistics(Population p) {
		super("Statistics");
		this.setBackground(Color.WHITE);
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		bestAdaptations = new Vector<Double>();
		worstAdaptations = new Vector<Double>();
		this.p = p;
		this.time = (double) System.currentTimeMillis();
	}

	/**
	 * Adds new adaptation to vector of best adaptations
	 * 
	 * @param a
	 *            adaptation (from 0 to 1)
	 */
	public void addBestAdaptation(double a) {
		bestAdaptations.add(a);
	}

	/**
	 * Adds new adaptation to vector of worst adaptations
	 * 
	 * @param a
	 *            adaptation (from 0 to 1)
	 */
	public void addWorstAdaptation(double a) {
		worstAdaptations.add(a);
	}

	/**
	 * Used to show Frame with results, such as time, number of generations etc
	 * 
	 * @param p
	 *            Last Generation
	 */
	public void showResults(Population p) {
		this.setTitle("Sequence reached in " + p.getGenerationNumber()
				+ " generation ");
		
		this.time = (double) System.currentTimeMillis() - this.time;
		this.time = (double) this.time / 1000;

		paintGraph( this.time);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setVisible(true);

	}

	/**
	 * Adds three panels with informations
	 * 
	 * @param adapt
	 *            vector of coordinates used to paint
	 * @param time
	 *            how much time did it take to solve tangram
	 */
	public void paintGraph(double time) {
		@SuppressWarnings("unchecked")
		DataTable dataTable = new DataTable(Integer.class, Double.class);
		for(int i = 0; i < bestAdaptations.size(); ++i) {
			dataTable.add( i + 1, bestAdaptations.get(i));
		}
		
		@SuppressWarnings("unchecked")
		DataTable dataTable2 = new DataTable(Integer.class, Double.class);
		for(int i = 0; i < worstAdaptations.size(); ++i) {
			dataTable2.add(i + 1, worstAdaptations.get(i));
		}
		
		DataSource data = new DataSeries("Best entity", dataTable);
		DataSource data2 = new DataSeries("Worst entity", dataTable2);
		
		Color color = new Color(0.0f, 0.3f, 1.0f);
		Color color2 = new Color(1.0f, 0.3f, 0.0f);
		
		XYPlot plot = new XYPlot(data, data2);
		
		LineRenderer lines = new DefaultLineRenderer2D();
		lines.setSetting(LineRenderer.COLOR, color);
		plot.setLineRenderer(data, lines);
		
		LineRenderer lines2 = new DefaultLineRenderer2D();
		lines2.setSetting(LineRenderer.COLOR, color2);
		plot.setLineRenderer(data2, lines2);
		
		AreaRenderer area = new DefaultAreaRenderer2D(); 
		area.setSetting(AreaRenderer.COLOR, GraphicsUtils.deriveWithAlpha(color, 64));
		plot.setAreaRenderer(data, area);
		
		AreaRenderer area2 = new DefaultAreaRenderer2D(); 
		area2.setSetting(AreaRenderer.COLOR, GraphicsUtils.deriveWithAlpha(color2, 64));
		plot.setAreaRenderer(data2, area2);
		
        plot.getPointRenderer(data).setSetting(PointRenderer.COLOR, color);
        plot.getPointRenderer(data2).setSetting(PointRenderer.COLOR, color2);
        
		plot.setSetting(XYPlot.TITLE, " Population statistics");
		plot.setSetting(XYPlot.TITLE_FONT, new Font(Font.SANS_SERIF, Font.BOLD, 24));
		plot.getAxisRenderer(XYPlot.AXIS_X).setSettingDefault(AxisRenderer.LABEL, "Generation");
		
		plot.getAxisRenderer(XYPlot.AXIS_Y).setSettingDefault(AxisRenderer.LABEL, "Adaptation [ % ]");
		plot.getAxisRenderer(XYPlot.AXIS_Y).setSetting(AxisRenderer.TICK_LABELS_FORMAT, new DecimalFormat("0 %"));
		plot.getAxisRenderer(XYPlot.AXIS_Y).setSetting(AxisRenderer.INTERSECTION, 1);
		
		plot.getAxis(XYPlot.AXIS_X).setRange(0, bestAdaptations.size() + 1);
		plot.getAxisRenderer(XYPlot.AXIS_X).setSetting(AxisRenderer.TICK_LABELS, true);
		plot.getAxisRenderer(XYPlot.AXIS_X).setSetting(AxisRenderer.TICKS, true);
		
		plot.getAxis(XYPlot.AXIS_Y).setRange(-0.15, 1.15);
		plot.getAxisRenderer(XYPlot.AXIS_Y).setSetting(AxisRenderer.TICKS, true);
		plot.getAxisRenderer(XYPlot.AXIS_Y).setSetting(AxisRenderer.TICK_LABELS, true);
		
		plot.setSetting(Plot.LEGEND, true);
		InteractivePanel panel = new InteractivePanel(plot);
		
		this.add(panel, BorderLayout.CENTER);	
		this.add(new UpInformation(time, p), BorderLayout.NORTH);
	}

	/**
	 * Adds panel with statistic informations
	 * 
	 * @author Marcin Kubik
	 * @author Mikołaj Markiewicz
	 * @author Krzysztof Opasiak
	 */
	private class UpInformation extends JPanel {

		public UpInformation(double time, Population p) {
			GridLayout l =new GridLayout(2, 2, 30, 30);
			this.setLayout(l);

			Font f = new Font(Font.SANS_SERIF, Font.BOLD, 16);
			
			JLabel timeLabel = new JLabel("Time of calculations: ", JLabel.CENTER);
			timeLabel.setFont(f);
			this.add(timeLabel);

			JLabel generationsLabel = new JLabel("Number of generations: \n\t\t", JLabel.CENTER);
			generationsLabel.setFont(f);
			this.add(generationsLabel);

			JLabel timeValue = new JLabel(time + " seconds", JLabel.CENTER);
			timeValue.setFont(f);
			this.add(timeValue);
			JLabel generationValue = new JLabel(p.getGenerationNumber() + " generations", JLabel.CENTER);
			generationValue.setFont(f);
			this.add(generationValue);
			

		}

		public void paintComponent(Graphics g) {}
	}
}
