#include "kgressourceloader.h"

KGRessourceLoader* KGRessourceLoader::p_Instance = 0;

KGRessourceLoader::KGRessourceLoader()
{

}


void KGRessourceLoader::loadRessources()
{
    p_map[ICON_NOTE] = QPixmap(ICON_NOTE);
    p_map[ICON_CALENDAR] = QPixmap(ICON_CALENDAR);
    p_map[BUTTON_FLOWPANEL] = QPixmap(BUTTON_FLOWPANEL);
}
