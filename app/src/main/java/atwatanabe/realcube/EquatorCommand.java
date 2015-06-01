package atwatanabe.realcube;

public class EquatorCommand
{
    private Cube3x3 cube;
    private boolean inverted;

    public EquatorCommand(Cube3x3 c, boolean invert)
    {
        cube = c;
        inverted = invert;
    }

    public void execute()
    {
        cube.equator(inverted);
    }

    public void unexecute()
    {
        cube.equator(!inverted);
    }
}
