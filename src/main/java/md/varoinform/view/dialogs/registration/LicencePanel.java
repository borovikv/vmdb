package md.varoinform.view.dialogs.registration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 12/18/13
 * Time: 1:50 PM
 */
public class LicencePanel extends CardPanel{
    private JCheckBox checkBox = new JCheckBox("I agree");

    public LicencePanel() {
        setLayout(new BorderLayout());
        add(new JScrollPane(label));
        add(checkBox, BorderLayout.SOUTH);
    }

    private String getLicence() {
        return "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus varius, lacus eu egestas viverra, dolor felis aliquam lacus, ut accumsan ipsum lectus ut risus. Morbi eget ornare est, vitae venenatis orci. In tristique interdum nunc id pellentesque. Fusce quis sem a purus aliquet fringilla. Fusce eget consectetur risus, ac rutrum ante. Ut congue ante vel dignissim ultrices. Proin quis consequat nisl, vitae cursus ante. Cras quis eros quis ligula commodo iaculis ac sit amet felis. Ut pretium lobortis turpis, id vulputate ipsum blandit ac. In metus augue, imperdiet in leo nec, hendrerit luctus mauris. Fusce orci orci, lobortis vitae facilisis suscipit, tincidunt et tellus.\n" +
                "\n" +
                "Phasellus vulputate at nunc eu suscipit. Pellentesque sit amet cursus augue. Morbi consectetur tincidunt nulla, nec sollicitudin felis blandit quis. Nulla sodales, purus quis eleifend pretium, lacus nisi laoreet massa, id molestie quam est ac quam. Donec molestie purus turpis, ut condimentum ipsum adipiscing id. Suspendisse pretium sit amet ante quis gravida. Vestibulum posuere porta neque, a mollis nisl eleifend vitae. Nam id enim aliquet, molestie lectus eget, accumsan enim. Nam sodales gravida malesuada. Vivamus sed tincidunt lacus. Suspendisse sed tincidunt tortor, sit amet mollis eros. Mauris dictum eu leo auctor blandit. Suspendisse varius ligula et tristique accumsan. Aliquam tempor tempus odio, ac pharetra dui vulputate eget. Etiam a gravida nisl. Nullam ac vehicula dui.\n" +
                "\n" +
                "Nam porttitor ac neque at ullamcorper. Sed elementum accumsan metus, a aliquam tellus vulputate id. Cras in nulla nulla. Donec volutpat a nibh ac scelerisque. Integer lacinia enim erat, at porta tellus ultrices quis. Duis cursus vel leo eu pretium. Suspendisse pellentesque, eros vel iaculis vulputate, nibh turpis condimentum massa, eu aliquet est diam a lectus. Morbi quis mi euismod, tempus tellus vitae, ultrices tortor.\n" +
                "\n" +
                "Interdum et malesuada fames ac ante ipsum primis in faucibus. Donec ultrices lacus mauris, at cursus erat vestibulum vel. Mauris urna orci, condimentum eu molestie vel, mollis adipiscing turpis. Cras sit amet dictum eros, nec scelerisque lectus. Mauris euismod erat id adipiscing tempor. Vestibulum massa lectus, consectetur a tempus eu, pharetra eu sapien. Pellentesque congue massa quis mauris fringilla vulputate. Donec lacinia, arcu eu pellentesque commodo, leo lectus feugiat lectus, at vulputate ipsum nunc et nulla.\n" +
                "\n" +
                "Vestibulum id mollis sapien, nec rhoncus ipsum. Nulla vel viverra lacus. Sed pretium ac purus varius posuere. Morbi nisi quam, laoreet at augue ut, vehicula pellentesque dui. Mauris ultricies convallis egestas. Fusce sit amet magna vitae est condimentum consequat a et nunc. Sed sollicitudin luctus porta. Proin nec orci interdum, congue nisl vel, suscipit ante. Suspendisse eget velit faucibus, eleifend tortor semper, dignissim felis. Maecenas sollicitudin ante ligula. Morbi varius nulla nec elit ullamcorper iaculis fermentum nec nulla. Praesent ullamcorper, dolor at congue ornare, nunc nunc fringilla purus, vitae consectetur leo nunc id tellus. Cras placerat, turpis a commodo facilisis, libero sapien mollis felis, at viverra odio nunc at lectus. Aliquam erat volutpat. Quisque ipsum nunc, dapibus a est nec, porta feugiat tellus.";
    }

    @Override
    public boolean isInputValid(){
        return checkBox.isSelected();
    }

    //ToDo:create real implementation
    @Override
    protected void setLabelText() {
        label.setText(getLicence());
    }

    public void addCheckBoxListener(ActionListener actionListener) {
        checkBox.addActionListener(actionListener);
    }
}
