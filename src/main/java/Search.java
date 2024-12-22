import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class Search extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {

        Editor editor = event.getData(PlatformDataKeys.EDITOR);
        if (editor == null) {
            return;
        }

        String selectedText = editor.getSelectionModel().getSelectedText();

        if (selectedText == null || selectedText.isEmpty()) {
            Messages.showMessageDialog("Текст не выбран!", "Ошибка", Messages.getErrorIcon());
            return;
        }

        String[] lines = selectedText.split("\n");

        StringBuilder searchQuery = new StringBuilder();

        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                try {
                    String encodedText = URLEncoder.encode(line, StandardCharsets.UTF_8.toString());
                    if (searchQuery.length() > 0) {
                        searchQuery.append("+");
                    }
                    searchQuery.append(encodedText);
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        if (searchQuery.length() > 0) {
            String searchUrl = "https://www.google.com/search?q=" + searchQuery.toString();
            BrowserUtil.browse(searchUrl);
        } else {
            Messages.showMessageDialog("Не удалось сформировать запрос для поиска!",
                    "Ошибка", Messages.getErrorIcon());
        }
    }
}
