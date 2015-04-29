package weka.core;

import weka.core.neighboursearch.PerformanceStats;

/**
 <!-- globalinfo-start -->
 * Implements the Continuous (or Generalized) Jaccard distance (or similarity) function.<br/>
 * <br/>
 * The standard formulation of the Jaccard index cannot operate on continuous values; This implementation generalizes the Jaccard similarity by borrowing the notion of a t-norm form the field of Fuzzy Logic.<br/>
 * <br/>
 * For more information, see:<br/>
 * <br/>
 * Toward Automatic Character Identification in Unannotated Narrative Text URL http://www.aaai.org/ocs/index.php/INT/INT7/paper/view/9253/9204.
 * <p/>
 <!-- globalinfo-end -->
 *
 <!-- technical-bibtex-start -->
 * BibTeX:
 * <pre>
 * &#64;inproceedings{valls2014,
 *    title={Toward automatic character identification in unannotated narrative text},
 *    author={Valls-Vargas, Josep and Onta{\~n}{\'o}n, Santiago and Zhu, Jichen},
 *    booktitle={Seventh Intelligent Narrative Technologies Workshop},
 *    year={2014},
 *    URL = {http://www.aaai.org/ocs/index.php/INT/INT7/paper/view/9253/9204}
 * }
 * </pre>
 * <p/>
 <!-- technical-bibtex-end -->
 *
 <!-- options-start -->
 <!-- options-end --> 
 *
 * @author Josep Valls-Vargas (josep@valls.name)
 * @version 1.0.0
 */
public class Wcjd extends NormalizableDistance
        implements Cloneable, TechnicalInformationHandler {

    protected double m_weights[] = null;
    protected boolean m_use_weights = false;

    public void setWeights(double weights[]) {
        m_weights = weights;
        m_use_weights = true;
    }

    @Override
    public double distance(Instance first, Instance second, double cutOffValue,
            PerformanceStats stats) {
        double inter = 0;
        double union = 0;
        int firstI, secondI;
        int firstNumValues = first.numValues();
        int secondNumValues = second.numValues();
        int numAttributes = m_Data.numAttributes();
        int classIndex = m_Data.classIndex();

        validate();

        for (int p1 = 0, p2 = 0; p1 < firstNumValues || p2 < secondNumValues;) {
            if (p1 >= firstNumValues) {
                firstI = numAttributes;
            } else {
                firstI = first.index(p1);
            }

            if (p2 >= secondNumValues) {
                secondI = numAttributes;
            } else {
                secondI = second.index(p2);
            }

            if (firstI == classIndex) {
                p1++;
                continue;
            }
            if ((firstI < numAttributes) && !m_ActiveIndices[firstI]) {
                p1++;
                continue;
            }

            if (secondI == classIndex) {
                p2++;
                continue;
            }
            if ((secondI < numAttributes) && !m_ActiveIndices[secondI]) {
                p2++;
                continue;
            }

            //double diff;

            if (firstI == secondI) {
                //diff = difference(firstI, first.valueSparse(p1), second.valueSparse(p2));
                p1++;
                p2++;
            } else {
                throw new UnsupportedOperationException("Wrong number of features.");
            }
            if (stats != null) {
                stats.incrCoordCount();
            }
            if (m_use_weights){
                inter += this.intersection(firstI, first.valueSparse(p1), second.valueSparse(p2)) * m_weights[firstI];
                union += this.union(firstI, first.valueSparse(p1), second.valueSparse(p2)) * m_weights[firstI];
            } else {
                inter += this.intersection(firstI, first.valueSparse(p1), second.valueSparse(p2));
                union += this.union(firstI, first.valueSparse(p1), second.valueSparse(p2));                
            }
        }
        return 1 - (inter / union);
    }

    protected double union(int index, double val1, double val2) {
        switch (m_Data.attribute(index).type()) {
            case Attribute.NUMERIC:
                if (Utils.isMissingValue(val1)) {
                    val1 = 0.0;
                }
                if (Utils.isMissingValue(val2)) {
                    val2 = 0.0;
                }
                return Math.max(val1, val2);
            default:
                return 0;
        }
    }

    protected double intersection(int index, double val1, double val2) {
        switch (m_Data.attribute(index).type()) {
            case Attribute.NUMERIC:
                if (Utils.isMissingValue(val1)) {
                    val1 = 0.0;
                }
                if (Utils.isMissingValue(val2)) {
                    val2 = 0.0;
                }
                return Math.min(val1, val2);
            default:
                return 0;
        }
    }

    @Override
    public String globalInfo() {
        return "Implements the Continuous (or Generalized) Jaccard distance (or similarity) function.\n\n"
                + "The standard formulation of the Jaccard index cannot operate on continuous values. This implementation generalizes the Jaccard similarity by borrowing the notion of a t-norm form the field of Fuzzy Logic.\n\n"
                + "For more information, see:\n\n"
                + getTechnicalInformation().toString();
    }

    @Override
    protected double updateDistance(double currDist, double diff) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getRevision() {
        return "1.0.0";
    }

    @Override
    public TechnicalInformation getTechnicalInformation() {
        TechnicalInformation result;
        result = new TechnicalInformation(TechnicalInformation.Type.INPROCEEDINGS);
        result = new TechnicalInformation(TechnicalInformation.Type.INPROCEEDINGS);
        result.setValue(TechnicalInformation.Field.AUTHOR, "Josep Valls-Vargas, Jichen Zhu and Santiago Ontañón");
        result.setValue(TechnicalInformation.Field.YEAR, "2014");
        result.setValue(TechnicalInformation.Field.TITLE, "Toward Automatic Character Identification in Unannotated Narrative Text");
        result.setValue(TechnicalInformation.Field.BOOKTITLE, "Intelligent Narrative Technologies 7: Papers from the 2014 Workshop");
        result.setValue(TechnicalInformation.Field.PUBLISHER, "AAAI");
        result.setValue(TechnicalInformation.Field.URL, "http://www.aaai.org/ocs/index.php/INT/INT7/paper/view/9253/9204");
        return result;
    }
}