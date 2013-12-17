/**
 * 
 */
package analytics.input;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import se.kb.oai.OAIException;
import se.kb.oai.pmh.ErrorResponseException;
import se.kb.oai.pmh.Header;
import se.kb.oai.pmh.IdentifiersList;
import se.kb.oai.pmh.OaiPmhServer;
import se.kb.oai.pmh.Record;

/**
 * @author vogias
 * 
 */
public class OAITargetInput extends Input {

	OaiPmhServer oaiPmhServer;

	/**
	 * @return the oaiPmhServer
	 */
	public OaiPmhServer getOaiPmhServer() {
		return oaiPmhServer;
	}

	/**
	 * @param oaiPmhServer
	 *            the oaiPmhServer to set
	 */
	public void setOaiPmhServer(OaiPmhServer oaiPmhServer) {
		this.oaiPmhServer = oaiPmhServer;
	}

	@Override
	public Collection<?> getData(String path, String repoSelection) {
		// TODO Auto-generated method stub

		oaiPmhServer = new OaiPmhServer(path);
		boolean more = true;
		Vector<String> data = new Vector<>();
		try {

			System.out.println("Prefix:" + repoSelection);
			IdentifiersList list = oaiPmhServer.listIdentifiers(repoSelection);

			if (list.size() == 0)
				System.err.println("There are no Identifiers");

			while (more) {
				for (Header header : list.asList()) {
					// System.out.println(header.getIdentifier());

					try {
						String identifier = header.getIdentifier();
						if (identifier == null)
							System.err.println("Identifier empty...");
						Record record = oaiPmhServer.getRecord(identifier,
								repoSelection);

						data.addElement(record.getMetadataAsString());
					} catch (ErrorResponseException ex) {
						ex.printStackTrace();
						continue;
					} catch (NullPointerException e) {
						e.printStackTrace();
						continue;
					}

				}
				if (list.getResumptionToken() != null)
					list = oaiPmhServer.listIdentifiers(list
							.getResumptionToken());
				else
					more = false;

			}

			return data;

		} catch (OAIException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return data;

		}

	}

	@Override
	public List<String> getRepoNames(String path) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		OAITargetInput input = new OAITargetInput();
		Collection<String> data = (Collection<String>) input
				.getData(
						"http://idea-pathway-creator.greenideasproject.org/greenideas/oai",
						"oai_lom");
		Iterator<String> iterator = data.iterator();

		while (iterator.hasNext()) {
			System.out.println(iterator.next());
		}
		System.out.println(input.getRepoName());
	}

	@Override
	public String getRepoName() {
		// TODO Auto-generated method stub
		OaiPmhServer server = getOaiPmhServer();
		String name;
		try {
			name = server.identify().getRepositoryName();
			return name;
		} catch (OAIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "UnknownRepoName";
		}

	}

}
