package org.jboss.tools.hb.ui.bot.test.completion;

import java.util.List;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.jboss.tools.hb.ui.bot.common.JPAEntity;
import org.jboss.tools.hb.ui.bot.common.Tree;
import org.jboss.tools.hb.ui.bot.test.HibernateBaseTest;
import org.jboss.tools.ui.bot.ext.config.Annotations.DB;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.gen.ActionItem;
import org.jboss.tools.ui.bot.ext.helper.StringHelper;
import org.jboss.tools.ui.bot.ext.parts.SWTBotEditorExt;
import org.junit.Test;

/**
 * Create JPA Entity ui bot test
 */
@Require(db = @DB, clearProjects = true)
public class AnnotationCodeCompletionTest extends HibernateBaseTest {
	
	final String prj = "jpatest35";
	final String pkg = "org.test.completion";
	final String ent = "CodeCompletionEntity.java";

	final String ext = ".java";
	
	@Test
	public void annotationCodeCompletionTest() {
		emptyErrorLog();
		importTestProject("/resources/prj/" + prj);
		importTestProject("/resources/prj/" + "hibernatelib");
		openJPAEntity();
		tryCodeCompletion();
		checkErrorLog();
	}

	private void tryCodeCompletion() {		
		SWTBotEditorExt editor = bot.swtBotEditorExtByTitle(ent);
		StringHelper sh = new StringHelper(editor.toTextEditor().getText());
		Point p = sh.getPositionAfter("@Entity");
		editor.toTextEditor().selectRange(p.y, p.x + 1, 0);
		String annoStart = "@Tab";
		editor.toTextEditor().insertText("\n" + annoStart);
		sh = new StringHelper(editor.toTextEditor().getText());
		p = sh.getPositionAfter(annoStart);
		editor.selectRange(p.y,p.x + 1,0);
		List<String> autoCompleteProposals = editor.getAutoCompleteProposals("");
	}

	private void openJPAEntity() {
		SWTBotView view = open.viewOpen(ActionItem.View.GeneralProjectExplorer.LABEL);
		Tree.open(view.bot(), prj, "src", pkg, ent);
	}
}
